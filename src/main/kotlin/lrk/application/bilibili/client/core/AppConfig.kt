package lrk.application.bilibili.client.core

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import lrk.application.bilibili.client.Platform
import java.io.File
import java.io.FileOutputStream

@Suppress("unused")
object AppConfig {
    const val DEBUG = true
    const val APP_NAME = "BiliBiliClient"
    const val APP_KEY = "1d8b6e7d45233436"
    const val APP_SEC = "560c52ccd288fed045859ed18bffd973"
    const val APP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:122.0) Gecko/20100101 Firefox/122.0"

    val APP_VIDEO_CACHE_FILE = File(Platform.getPlatformDataDir().path + File.separator + "Cache.mp4")
    val APP_VIDEO_MRL: String = APP_VIDEO_CACHE_FILE.absolutePath

    var cookie: MutableMap<String, ArrayList<LinkedTreeMap<String, String>>> = mutableMapOf()
    val cookieFile: File = File(Platform.getPlatformDataDir().path + File.separator + "cookies")

    fun saveCookie() {
        if (!cookieFile.exists()) cookieFile.createNewFile()
        FileOutputStream(cookieFile).use {
            it.write(Gson().toJson(cookie).toByteArray(Platform.getPlatformCharset()))
        }
    }

}