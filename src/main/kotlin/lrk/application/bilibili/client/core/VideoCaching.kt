package lrk.application.bilibili.client.core

import com.google.gson.JsonParser
import lrk.application.bilibili.client.api.BilibiliApi
import lrk.application.bilibili.client.api.getVideoURL
import lrk.application.bilibili.client.core.log.Log
import java.io.FileOutputStream


fun startVideoCachingProcess(bvid: String, cid: Int, qn: Int) {
    cleanVideoCache()
    APP_GLOBAL_VIDEO_CACHING_PROCESS_THREAD_POOL.execute {
        AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD = Thread.currentThread()
        val videoUrl = getVideoURL(
            JsonParser.parseString(
                Client.getClient().newCall(
                    makeGetRequestWithCookie(
                        makeGetURLWithWbi(
                            BilibiliApi.API_GET_VIDEO_ADDRESS_URL,
                            "bvid" to bvid,
                            "cid" to cid,
                            "qn" to qn
                        )
                    )
                ).execute().body.string()
            ).asJsonObject.get("data").asJsonObject
        )
        Log.d("Video URL: $videoUrl")
        val response = Client.getClient().newCall(makeGetRequestWithCookie(videoUrl)).execute()
        val sourceStream = response.body.byteStream()

        AppState.VideoProcessState.CURRENT_SOURCE_STREAM = sourceStream
        AppState.VideoProcessState.SERVER_PREPARED = true
        if (!AppConfig.APP_VIDEO_CACHE_FILE.exists()) {
            AppConfig.APP_VIDEO_CACHE_FILE.createNewFile()
        } else {
            AppConfig.APP_VIDEO_CACHE_FILE.delete()
            AppConfig.APP_VIDEO_CACHE_FILE.createNewFile()
        }
        val targetStream = FileOutputStream(AppConfig.APP_VIDEO_CACHE_FILE)
        val buffer = ByteArray(1024)

        Log.d("Video Caching Process Started")

        try {
            while (!Thread.currentThread().isInterrupted) {
                val len = sourceStream.read(buffer) // "stream closed" exception will be ignored
                if (len != -1 && !Thread.currentThread().isInterrupted) {
                    targetStream.write(buffer, 0, len)
                } else {
                    break
                }
            }
        } catch (_: Exception) {

        }

        targetStream.close()
        sourceStream.close()
        response.close()

        Log.d("Video Caching Process End")
    }
}

fun cleanVideoCache() {
    AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD?.interrupt()
    AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD = null
    AppState.VideoProcessState.SERVER_PREPARED = false
    AppState.VideoProcessState.CURRENT_SOURCE_STREAM?.close()
    if (AppConfig.APP_VIDEO_CACHE_FILE.exists()) AppConfig.APP_VIDEO_CACHE_FILE.delete()
}