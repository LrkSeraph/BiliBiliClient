package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.ui.theme.AppTheme
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.api.Login
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.ui.components.QRCodeWaitForScan
import java.util.*

@Composable
fun LoginPage(loginStatus: MutableState<Boolean>) {
    val url: MutableState<String> = remember {
        val response = Login.requestLoginQRCodeInfo()
        when (response["status"]) {
            "0" -> {
                AppState.LoginState.qrcodeKey = response["qrcode_key"]!!
                mutableStateOf(response["url"]!!)
            }

            else -> {
                mutableStateOf("")
            }
        }
    }
    val text = remember {
        when (url.value) {
            "" -> {
                mutableStateOf("请求失败, 请稍后再试")
            }

            else -> {
                mutableStateOf("未扫码")
            }
        }
    }

    AppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(10.dp.platformScaled()))
                Text(text = "Login", fontWeight = FontWeight.Bold, fontSize = TextUnit(20f, TextUnitType.Sp).platformScaled())
                Spacer(modifier = Modifier.height(10.dp.platformScaled()))
                QRCodeWaitForScan(url, text, 200)
            }
            LaunchedEffect(Unit) {
                QRCodeLoginTimer.start(text, loginStatus)
            }
            DisposableEffect(Unit){
                onDispose {
                    QRCodeLoginTimer.timer.cancel()
                }
            }
        }
    }
}

object QRCodeLoginTimer {
    val timer = Timer("QRCodeLoginTimer", true)
    fun start(text: MutableState<String>, loginStatus: MutableState<Boolean>) {
        timer.schedule(object : TimerTask() {
            override fun run() {
                val response = Login.checkQRCodeScanState(AppState.LoginState.qrcodeKey)
                when (response["status"]) {
                    "0" -> {
                        text.value = response["message"]!!
                        when (response["code"]) {
                            "0" -> { // login successful
                                text.value = "扫码登录成功"
                                loginStatus.value = true
                            }

                            "86038" -> { // qrcode is no longer valid
                                text.value = "二维码已失效"
                            }

                            "86090" -> { // qrcode was scanned, but haven't confirm
                                text.value = "二维码已扫码未确认"
                            }

                            "86101" -> { // not scanned
                                text.value = "未扫码"
                            }
                        }
                    }

                    "1" -> {
                        text.value = response["message"]!!
                    }

                    else -> {
                        text.value = "请求失败, 请稍后再试"
                    }
                }
            }
        }, 0L, 2000L)
    }
}