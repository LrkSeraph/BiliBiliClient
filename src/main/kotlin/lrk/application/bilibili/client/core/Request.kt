package lrk.application.bilibili.client.core

import lrk.application.bilibili.client.api.BilibiliApi
import lrk.application.bilibili.client.api.getNavigationUserInfo
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.HashMap

fun getAllCookieString(domain: String): String {
    val stringBuilder = StringBuilder()
    AppConfig.cookie[domain]?.forEach {
        stringBuilder.append("${it["name"]}=${it["value"]}; ")
    }
    return stringBuilder.toString()
}

fun makeGetRequestWithCookie(url: String): Request {
    return Request.Builder()
        .get()
        .url(url)
        .addHeader("Referer", "https://www.bilibili.com/")
        .addHeader("User-Agent", AppConfig.APP_USER_AGENT)
        .addHeader("Cookie", getAllCookieString("passport.bilibili.com"))
        .build()
}

fun makePostRequestWithCookie(url: String, requestBody: RequestBody): Request {
    return Request.Builder()
        .post(requestBody)
        .url(url)
        .addHeader("Referer", "https://www.bilibili.com/")
        .addHeader("User-Agent", AppConfig.APP_USER_AGENT)
        .addHeader("Cookie", getAllCookieString("passport.bilibili.com"))
        .build()
}

fun makeGetURL(url: String, vararg params: Pair<String, String>): String {
    val stringBuilder = StringBuilder(url)
    params.forEachIndexed { index, it ->
        stringBuilder.append(if (index == 0) "?" else "&")
        stringBuilder.append("${it.first}=${URLEncoder.encode(it.second, StandardCharsets.UTF_8)}")
    }
    return stringBuilder.toString()
}

fun makeGetURLWithWbi(url: String, vararg params: Pair<String, Any>): String {
    val navigationUserInfoObj = BilibiliApi.getNavigationUserInfo()!!
    val paramsMap = HashMap<String, Any>()
    params.forEach { (k, v) ->
        paramsMap[k] = v
    }
    return "$url?${
        Algorithm.wbi(
            navigationUserInfoObj.wbi_img.img_url,
            navigationUserInfoObj.wbi_img.sub_url,
            paramsMap
        )
    }"
}