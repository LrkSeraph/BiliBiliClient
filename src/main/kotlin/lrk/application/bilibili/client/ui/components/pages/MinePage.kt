package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.ui.components.BottomBar
import lrk.application.bilibili.client.ui.components.UserInfoBlock

@Composable
fun MinePage() {
    val drawerState = DrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomBar(height = 50.dp)
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                UserInfoBlock()
            }
        }
    }
}
