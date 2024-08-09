package lrk.application.bilibili.client.core

fun getCookie(domain: String, name: String): String {
    AppConfig.cookie[domain]?.forEach {
        if (it["name"] == name) {
            return it["value"].toString()
        }
    }
    return ""
}