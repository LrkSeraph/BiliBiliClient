package lrk.application.bilibili.client.core

import lrk.application.bilibili.client.core.log.Log
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

object Algorithm {

    fun signedQueryString(params: MutableMap<String, String>): String {
        params["appkey"] = AppConfig.APP_KEY
        val sortedParams: TreeMap<String, String> = TreeMap<String, String>(params)
        val queryBuilder = StringBuilder()
        for ((key, value) in sortedParams) {
            if (queryBuilder.isNotEmpty()) {
                queryBuilder.append('&')
            }
            queryBuilder
                .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                .append('=')
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8))
        }
        return queryBuilder.toString() + "&sign=${generateMD5(queryBuilder.append(AppConfig.APP_SEC).toString())}"
    }

    private fun generateMD5(input: String): String {
        val md5 = MessageDigest.getInstance("MD5")
        val digest = md5.digest(input.toByteArray())
        val stringBuilder = StringBuilder()
        digest.forEach {
            stringBuilder.append(String.format("%02x", it))
        }
        return stringBuilder.toString()
    }

    fun correspondPath(timestamp: Long): String {
        val publicKeyPEM = """
        -----BEGIN PUBLIC KEY-----
        MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLgd2OAkcGVtoE3ThUREbio0Eg
        Uc/prcajMKXvkCKFCWhJYJcLkcM2DKKcSeFpD/j6Boy538YXnR6VhcuUJOhH2x71
        nzPjfdTcqMz7djHum0qSZA0AyCBDABUqCrfNgCiJ00Ra7GmRj+YCK1NJEuewlb40
        JNrRuoEUXpabUzGB8QIDAQAB
        -----END PUBLIC KEY-----
    """.trimIndent()

        val publicKey = KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(
                Base64.getDecoder().decode(
                    publicKeyPEM
                        .replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replace("\n", "")
                        .trim()
                )
            )
        )

        val cipher = Cipher.getInstance("RSA/ECB/OAEPPadding").apply {
            init(
                Cipher.ENCRYPT_MODE,
                publicKey,
                OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT)
            )
        }

        return cipher.doFinal("refresh_$timestamp".toByteArray()).joinToString("") { "%02x".format(it) }
    }

    // Wbi鉴权
    private val mixinKeyEncTab = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58, 3, 45, 35, 27, 43, 5, 49,
        33, 9, 42, 19, 29, 28, 14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55, 40,
        61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54, 21, 56, 59, 6, 63, 57, 62, 11,
        36, 20, 34, 44, 52
    )

    private fun getMixinKey(imgKey: String, subKey: String): String {
        val s = imgKey + subKey
        val key = StringBuilder()
        for (i in 0..31) {
            key.append(s[mixinKeyEncTab[i]])
        }
        return key.toString()
    }

    fun wbi(imgUrl: String, subUrl: String, params: HashMap<String, Any>): String {
        val mixinKey = getMixinKey(
            imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.lastIndexOf(".")),
            subUrl.substring(subUrl.lastIndexOf("/") + 1, subUrl.lastIndexOf("."))
        )
        params["wts"] = System.currentTimeMillis() / 1000
        val result = StringJoiner("&")
        params.entries.stream()
            .sorted(java.util.Map.Entry.comparingByKey<String, Any>())
            .forEach { entry: Map.Entry<String, Any> ->
                result.add(
                    entry.key + "=" + URLEncoder.encode(entry.value.toString(), StandardCharsets.UTF_8)
                )
            }
        val s = result.toString() + mixinKey
        val wbiSign: String = generateMD5(s)
        val finalParam = "$result&w_rid=$wbiSign".replace("+", "%20")
        Log.d("WBI signed: $finalParam")
        return finalParam
    }

}