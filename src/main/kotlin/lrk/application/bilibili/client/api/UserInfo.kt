package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.log.Log
import lrk.application.bilibili.client.core.makeGetRequestWithCookie
import lrk.application.bilibili.client.core.makeGetURLWithWbi
import lrk.application.bilibili.client.core.obj.NavigationUserInfoObj
import lrk.application.bilibili.client.core.obj.UserSpaceInfoObj

fun BilibiliApi.getNavigationUserInfo(): NavigationUserInfoObj? {
    Log.d("BilibiliApi::getNavigationUserInfo invoked")
    val request = makeGetRequestWithCookie(API_USER_NAVIGATION_INFORMATION_URL)
    val response = Client.getClient().newCall(request).execute()
    val result = when (response.isSuccessful) {
        true -> {
            val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
            if (responseJsonObject.get("code").asInt == 0) {
                NavigationUserInfoObj.getInstance(responseJsonObject.get("data").asJsonObject)
            } else {
                AppState.LoginState.shouldReLogin.value = true
                null
            }
        }

        false -> null
    }
    response.close()
    return result
}

fun BilibiliApi.getUserSpaceInfo(mid: Int): UserSpaceInfoObj?{
    Log.d("BilibiliApi::getUserSpaceInfo invoked")
    val request = makeGetRequestWithCookie(makeGetURLWithWbi(API_USER_SPACE_INFORMATION_URL, "mid" to mid))
    val response = Client.getClient().newCall(request).execute()
    val result = when (response.isSuccessful) {
        true -> {
            val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
            if (responseJsonObject.get("code").asInt == 0) {
                UserSpaceInfoObj.getInstance(responseJsonObject.get("data").asJsonObject)
            } else {
                AppState.LoginState.shouldReLogin.value = true
                null
            }
        }

        false -> null
    }
    response.close()
    return result
}