package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.getCookie
import lrk.application.bilibili.client.core.log.Log
import lrk.application.bilibili.client.core.makePostRequestWithCookie
import okhttp3.FormBody

fun BilibiliApi.vipSignIn(): Boolean {
    val request =
        makePostRequestWithCookie(API_VIP_SIGN, FormBody.Builder().addEncoded("csrf", getCookie("passport.bilibili.com", "bili_jct")).build())
    val response = Client.getClient().newCall(request).execute()
    val result = when (response.isSuccessful) {
        true -> {
            val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
            val code = responseJsonObject.get("code").asInt
            if (code == 0) {
                true
            } else {
                Log.i(responseJsonObject.get("message").asString)
                false
            }
        }

        false -> false
    }
    response.close()
    return result
}