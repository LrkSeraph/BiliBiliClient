package lrk.application.bilibili.client.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.ui.components.pages.VideoPlayerPage

data class VideoPlayerScreen(val url: String, val videoInfoObj: RecommendVideoInfoObj) : Screen {

    @Composable
    override fun Content() {
        VideoPlayerPage(url = url, videoInfoObj = videoInfoObj)
    }

}