package lrk.application.bilibili.client.core

import com.google.gson.internal.LinkedTreeMap
import okhttp3.*

object Client {
    private var client: OkHttpClient? = null
    fun getClient(): OkHttpClient {
        if (client == null) {
                client = OkHttpClient.Builder().cookieJar(object : CookieJar {
                // useless cookie setting, please use makeRequest(url: String) instead, it will set cookie in request header
                override fun loadForRequest(url: HttpUrl): List<Cookie> {
                    val cookie = AppConfig.cookie[url.host] ?: ArrayList()
                    val result = ArrayList<Cookie>()
                    cookie.forEach {
                        val builder = Cookie.Builder()
                        builder
                            .name(it["name"]!!)
                            .value(it["value"]!!)
                            .expiresAt(it["expiresAt"]!!.toLong())
                            .domain(it["domain"]!!)
                            .path(it["path"]!!)
                        if (it["secure"]!!.toBoolean()) builder.secure()
                        if (it["httpOnly"]!!.toBoolean()) builder.httpOnly()
                        if (it["hostOnly"]!!.toBoolean()) builder.httpOnly()
                        result.add(
                            builder.build()
                        )
                    }
                    return result
                }

                override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                    if (AppConfig.cookie[url.host] == null) AppConfig.cookie[url.host] = ArrayList()
                    AppConfig.cookie[url.host].apply {
                        cookies.forEach {
                            val cookieEntry = LinkedTreeMap<String, String>().apply {
                                put("name", it.name)
                                put("value", it.value)
                                put("expiresAt", it.expiresAt.toString())
                                put("domain", it.domain)
                                put("path", it.path)
                                put("secure", it.secure.toString())
                                put("httpOnly", it.httpOnly.toString())
                                put("persistent", it.persistent.toString())
                                put("hostOnly", it.hostOnly.toString())
                            }
                            val index = this?.getEntryIndex(cookieEntry)
                            if (index != -1) {
                                index?.let { it1 -> this?.removeAt(it1) }
                            }
                            this?.add(cookieEntry)
                        }
                    }
                }

                private fun java.util.ArrayList<LinkedTreeMap<String, String>>.getEntryIndex(entry: LinkedTreeMap<String, String>): Int {
                    this.forEachIndexed { index, it ->
                        if (it["name"] == entry["name"]) return index
                    }
                    return -1
                }
            }).build()
        }
        return client!!
    }
}