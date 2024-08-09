package lrk.application.bilibili.client.api

import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.makeGetRequestWithCookie
import lrk.application.bilibili.client.core.makeGetURL

fun BilibiliApi.search(keyword: String) {
    val request = makeGetRequestWithCookie(makeGetURL(API_SEARCH, "keyword" to keyword))
    val response = Client.getClient().newCall(request).execute()
    when (response.isSuccessful) {
        true -> {
            println(response.body.string())
        }

        false -> {

        }
    }
}