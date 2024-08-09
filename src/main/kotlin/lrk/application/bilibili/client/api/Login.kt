package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.*
import okhttp3.Request

object Login {
    private val client = Client.getClient()

    /*
    *   get qrcode login info
    * status: if -1, request failed; if 1, response body contains error message; if 0, success
     */
    fun requestLoginQRCodeInfo(): Map<String, String> {
        val request = Request.Builder().url(BilibiliApi.API_QRCODE_LOGIN_URL).get().build()
        val response = client.newCall(request).execute()
        val result = when (response.isSuccessful) {
            true -> {
                val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
                if (responseJsonObject.get("code").asInt != 0) {
                    mapOf("status" to "1", "message" to responseJsonObject.get("message").asString)
                } else {
                    val url = responseJsonObject.get("data").asJsonObject.get("url").asString
                    val qrcodeKey = responseJsonObject.get("data").asJsonObject.get("qrcode_key").asString
                    mapOf("status" to "0", "url" to url, "qrcode_key" to qrcodeKey)
                }
            }

            false -> mapOf("status" to "-1")
        }
        response.close()
        return result
    }

    fun checkQRCodeScanState(qrcodeKey: String): Map<String, String> {
        val request = Request.Builder().url(makeGetURL(BilibiliApi.API_QRCODE_CHECK_URL, "qrcode_key" to qrcodeKey)).get().build()
        val response = client.newCall(request).execute()
        val result = when (response.isSuccessful) {
            true -> {
                val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
                if (responseJsonObject.get("code").asInt != 0) {
                    mapOf("status" to "1", "message" to responseJsonObject.get("message").asString)
                } else {
                    val url = responseJsonObject.get("data").asJsonObject.get("url").asString
                    val refreshToken = responseJsonObject.get("data").asJsonObject.get("refresh_token").asString
                    val timestamp = responseJsonObject.get("data").asJsonObject.get("timestamp").asInt
                    val code = responseJsonObject.get("data").asJsonObject.get("code").asInt
                    val message = responseJsonObject.get("data").asJsonObject.get("message").asString
                    if (code == 0) {
                        mapOf(
                            "status" to "0",
                            "url" to url,
                            "refresh_token" to refreshToken,
                            "timestamp" to timestamp.toString(),
                            "code" to "0",
                            "message" to message
                        )
                    } else {
                        mapOf("status" to "0", "code" to code.toString(), "message" to message)
                    }
                }
            }

            false -> mapOf("status" to "-1")
        }
        response.close()
        return result
    }

    fun checkIsLogin(): Boolean{
        val request = makeGetRequestWithCookie(BilibiliApi.API_USER_NAVIGATION_INFORMATION_URL)
        val response = client.newCall(request).execute()
        val result = when (response.isSuccessful) {
            true -> {
                val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
                responseJsonObject.get("code").asInt == 0
            }
            false -> false
        }
        response.close()
        return result
    }
}