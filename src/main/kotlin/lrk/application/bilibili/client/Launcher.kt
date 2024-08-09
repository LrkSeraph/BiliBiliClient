@file:JvmName("Launcher")

package lrk.application.bilibili.client

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.api.BilibiliApi
import lrk.application.bilibili.client.api.Login
import lrk.application.bilibili.client.api.search
import lrk.application.bilibili.client.api.vipSignIn
import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.Initialize
import lrk.application.bilibili.client.ui.components.pages.LoginPage
import lrk.application.bilibili.client.ui.components.pages.MainPage
import lrk.application.bilibili.client.ui.theme.AppTheme

@Composable
@Preview
fun App() {
    MainPage()
    LaunchedEffect(Unit) {

    }
}

fun main() {
    Initialize.initialize()
    println("大积分签到: ${BilibiliApi.vipSignIn()}")
    BilibiliApi.search("Star Rail")
    application {
        val loginStatus = remember { mutableStateOf(Login.checkIsLogin()) }
        var showLoginWindow by remember { mutableStateOf(!Login.checkIsLogin()) }
        val mainWindowState = rememberWindowState().also {
            it.size = DpSize(1440.dp.platformScaled(), 810.dp.platformScaled())
        }
        if (loginStatus.value) {
            showLoginWindow = false
            Window(title = AppConfig.APP_NAME,
                state = mainWindowState,
                onCloseRequest = {
                    AppConfig.saveCookie()
                    exitApplication()
                }) {
                App()
            }
        }
        // Login Window
        if (showLoginWindow) {
            val loginWindowState = rememberWindowState()
            loginWindowState.size = DpSize(200.dp.platformScaled(), 300.dp.platformScaled())
            loginWindowState.position = WindowPosition(Alignment.Center)
            Window(title = "Login",
                resizable = false,
                undecorated = true,
                transparent = true,
                state = loginWindowState,
                onCloseRequest = {
                    AppConfig.saveCookie()
                    showLoginWindow = false
                }) {
                AppTheme {
                    Surface(modifier = Modifier.fillMaxSize(), shape = RoundedCornerShape(8.dp)) {
                        WindowDraggableArea {
                            BoxWithConstraints {
                                Column(
                                    modifier = Modifier,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                        // button for close and minimize
                                        TextButton(
                                            modifier = Modifier.width(20.dp.platformScaled()).height(20.dp.platformScaled()),
                                            contentPadding = PaddingValues(0.dp),
                                            onClick = {
                                                loginWindowState.isMinimized = true
                                            }) {
                                            Icon(
                                                modifier = Modifier.fillMaxSize(),
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null
                                            )
                                        }
                                        TextButton(
                                            modifier = Modifier.width(20.dp.platformScaled()).height(20.dp.platformScaled()),
                                            contentPadding = PaddingValues(0.dp),
                                            onClick = {
                                                exitApplication()
                                            }) {
                                            Icon(
                                                modifier = Modifier.fillMaxSize(),
                                                imageVector = Icons.Default.Close,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                    // show qrcode login page
                                    Box(
                                        modifier = Modifier.width(this@BoxWithConstraints.maxWidth)
                                            .height(this@BoxWithConstraints.maxHeight)
                                            .padding(start = 15.dp.platformScaled(), end = 15.dp.platformScaled(), bottom = 15.dp.platformScaled())
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                        ) {
                                            LoginPage(loginStatus = loginStatus)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}