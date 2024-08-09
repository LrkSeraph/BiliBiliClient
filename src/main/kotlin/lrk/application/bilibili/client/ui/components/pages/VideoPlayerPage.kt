package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.delay
import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.ui.components.VideoPlayerPageTopBar
import lrk.application.bilibili.client.ui.components.videoplayer.VideoPlayer
import lrk.application.bilibili.client.ui.components.videoplayer.getAppSink
import lrk.application.bilibili.client.ui.theme.AppTheme
import org.freedesktop.gstreamer.elements.PlayBin


@Composable
fun VideoPlayerPage(url: String, videoInfoObj: RecommendVideoInfoObj) {
    val navigator = LocalNavigator.current
    val appSink = getAppSink("VideoPlayer")
    val playBin = PlayBin("VideoPlayer")
    AppTheme {
        Scaffold(topBar = {
            VideoPlayerPageTopBar(navigator = navigator, title = videoInfoObj.title)
        }) {
            BoxWithConstraints {
                Row(
                    modifier = Modifier.width(this.maxWidth).height(this.maxHeight)
                ) {
                    Column(
                        modifier = Modifier.height(this@BoxWithConstraints.maxHeight)
                    ) {
                        VideoPlayer(
                            appSink = appSink,
                            width = Dp(this@BoxWithConstraints.maxWidth.value),
                            height = Dp(this@BoxWithConstraints.maxHeight.value)
                        )
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            playBin.apply {
                setVideoSink(appSink)
                setURI(AppConfig.APP_VIDEO_CACHE_FILE.toURI())
            }
            // TODO: fix this
            while (true){
                delay(1000)
                if (!playBin.isPlaying) {
                    playBin.play()
                }else{
                    break
                }
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                appSink.stop()
                playBin.stop()
                appSink.dispose()
                playBin.dispose()
            }
        }
    }
}
