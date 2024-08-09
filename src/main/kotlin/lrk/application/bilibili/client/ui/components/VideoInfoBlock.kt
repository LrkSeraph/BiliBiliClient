package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.api.BilibiliApi
import lrk.application.bilibili.client.api.PictureTools
import lrk.application.bilibili.client.api.formatDuration
import lrk.application.bilibili.client.api.getPicture
import lrk.application.bilibili.client.core.APP_GLOBAL_NETWORK_THREAD_POOL
import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.log.Log
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.core.startVideoCachingProcess
import lrk.application.bilibili.client.ui.navigation.VideoPlayerScreen
import java.lang.Integer.max

@Composable
fun VideoInfoBlock(width: Dp = 320.dp, height: Dp = 100.dp, modifier: Modifier = Modifier, videoInfoObj: RecommendVideoInfoObj, id: Int) {
    val navigator = LocalNavigator.currentOrThrow
    var videoPic by remember {
        mutableStateOf(PictureTools.getEmptyImageBitmap(250, 200))
    }
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        shape = RoundedCornerShape(15.dp),
        tonalElevation = 5.dp,
        modifier = modifier.width(width.platformScaled()).height(height.platformScaled()).clickable {
        Log.i("VideoInfoBlock was clicked: ${videoInfoObj.title}, ${videoInfoObj.bvid}")
        startVideoCachingProcess(videoInfoObj.bvid, videoInfoObj.cid, 112)
        navigator.push(
            VideoPlayerScreen(
                AppConfig.APP_VIDEO_MRL,
                videoInfoObj
            )
        )
    }) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                modifier = Modifier.fillMaxHeight().width(150.dp),
                bitmap = videoPic,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // title
                Text(
                    modifier = Modifier.fillMaxWidth().padding(end = 2.dp),
                    text = videoInfoObj.title,
                    fontSize = TextUnit(15F, TextUnitType.Sp).platformScaled(),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                // video info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "UP: ${videoInfoObj.owner.name}",
                        fontSize = TextUnit(8F, TextUnitType.Sp).platformScaled(),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "时长: ${formatDuration(videoInfoObj.duration)}",
                        fontSize = TextUnit(10F, TextUnitType.Sp).platformScaled()
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${videoInfoObj.stat.view}播放 ${videoInfoObj.stat.like}点赞 ${videoInfoObj.stat.danmaku}弹幕",
                        fontSize = TextUnit(10F, TextUnitType.Sp).platformScaled()
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        AppState.RecommendVideoPoolState.currentScrollIndex.value =
            max(id, AppState.RecommendVideoPoolState.currentScrollIndex.value)
        APP_GLOBAL_NETWORK_THREAD_POOL.execute {
            videoPic = BilibiliApi.getPicture(videoInfoObj.pic, 250, 200)!!
        }
    }
}