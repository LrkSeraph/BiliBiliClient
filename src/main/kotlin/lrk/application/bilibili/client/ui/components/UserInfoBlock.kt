package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.api.*
import lrk.application.bilibili.client.core.APP_GLOBAL_NETWORK_THREAD_POOL
import lrk.application.bilibili.client.core.obj.NavigationUserInfoObj

@Composable
fun UserInfoBlock(width: Dp = 250.dp, height: Dp = 100.dp) {
    var face by remember {
        mutableStateOf(PictureTools.getEmptyImageBitmap(256, 256))
    }
    var pendant by remember {
        mutableStateOf(PictureTools.getEmptyImageBitmap(256, 256))
    }
    var uname by remember {
        mutableStateOf("")
    }
    var answerStatus by remember {
        mutableStateOf("")
    }
    var bcoin_balance by remember {
        mutableStateOf(0)
    }
    var money by remember {
        mutableStateOf(0)
    }
    var levelImagePath by remember {
        mutableStateOf("assets/level-images/lv3.png")
    }

    Column(modifier = Modifier.width(width.platformScaled()).height(height.platformScaled())) {
        Row(
            modifier = Modifier.padding(start = 5.dp, top = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Avatar(face, pendant, 80.dp.platformScaled(), 80.dp.platformScaled())
            Spacer(modifier = Modifier.width(5.dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = uname, fontSize = TextUnit(17F, TextUnitType.Sp).platformScaled())
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        modifier = Modifier.height(20.dp),
                        painter = painterResource(levelImagePath),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Surface(shape = RoundedCornerShape(2.dp)) {
                    Text(
                        modifier = Modifier.border(1.dp, Color.Black).padding(1.dp),
                        text = answerStatus,
                        fontSize = TextUnit(10F, TextUnitType.Sp).platformScaled()
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "B币: ${String.format("%.1f", bcoin_balance.toFloat())}",
                        fontSize = TextUnit(12F, TextUnitType.Sp).platformScaled()
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "硬币: $money", fontSize = TextUnit(12F, TextUnitType.Sp).platformScaled())
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        APP_GLOBAL_NETWORK_THREAD_POOL.execute{
            val userinfo = BilibiliApi.getNavigationUserInfo()!!
            BilibiliApi.getPicture(userinfo.face)?.let { face = it }
            BilibiliApi.getPicture(userinfo.pendant.image)?.let { pendant = it }
            uname = userinfo.uname
            answerStatus = if (userinfo.answer_status == 0) "正式会员" else "未答题"
            bcoin_balance = userinfo.wallet.bcoin_balance
            money = userinfo.money
            levelImagePath = when (userinfo.level_info.current_level) {
                6 -> {
                    if (userinfo.is_senior_member == NavigationUserInfoObj.SeniorMemberStatus.YES)
                        "assets/level-images/lv6s.png"
                    else
                        "assets/level-images/lv6.png"
                }
                else -> {
                    "assets/level-images/lv${userinfo.level_info.current_level}.png"
                }
            }
        }
    }
}