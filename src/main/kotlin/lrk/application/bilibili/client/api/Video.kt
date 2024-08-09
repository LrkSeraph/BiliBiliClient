package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.getCookie
import lrk.application.bilibili.client.core.makeGetRequestWithCookie
import lrk.application.bilibili.client.core.makeGetURL
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import okhttp3.Request

fun BilibiliApi.getRecommendVideo(): ArrayList<RecommendVideoInfoObj> {
    val result = ArrayList<RecommendVideoInfoObj>()
    // when there is  no cookie[SESSDATA] for authentication, use default request to get recommend videos
    val request = if (getCookie("passport.bilibili.com", "SESSDATA") == "") {
        Request.Builder().url(API_GET_RECOMMEND_VIDEOS_URL).get().build()
    } else {
        makeGetRequestWithCookie(makeGetURL(API_GET_RECOMMEND_VIDEOS_URL, "ps" to "14", "fresh_type" to "3"))
    }
    val response = Client.getClient().newCall(request).execute()
    if (response.isSuccessful) {
        val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
        if (responseJsonObject.get("code").asInt == 0) {
            val item = responseJsonObject.get("data").asJsonObject.get("item").asJsonArray
            item.forEach {
                val obj = RecommendVideoInfoObj.getInstance(it.asJsonObject)
                result.add(obj)
            }
        }
    }
    response.close()
    return result
}

fun getVideoURL(data: JsonObject): String {
    return data.get("durl").asJsonArray[0].asJsonObject.get("url").asString
}

fun formatDuration(duration: Int): String {
    return when {
        duration < 60 -> {
            "${duration}秒"
        }

        duration < 3600 -> {
            val minute = duration.div(60)
            val second = duration - 60 * minute
            "${minute}分${second}秒"
        }

        else -> {
            val hour = duration.div(3600)
            val minute = (duration - hour * 3600).div(60)
            val second = duration - hour * 3600 - minute * 60
            "${hour}小时${minute}分${second}秒"
        }
    }
}
