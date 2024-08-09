package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.ui.navigation.HomeScreen
import lrk.application.bilibili.client.ui.navigation.MineScreen

@Composable
fun BottomBar(height: Dp = 50.dp) {
    val navigator = LocalNavigator.currentOrThrow
    NavigationBar(
        modifier = Modifier.height(height.platformScaled()),
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        NavigationBarItem(
            selected = false, onClick = {
                if (navigator.items.contains(HomeScreen)) {
                    navigator.popUntil { it is HomeScreen }
                } else {
                    navigator.push(HomeScreen)
                }
            }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = null)
                    Text(text = "首页", fontSize = TextUnit(13F, TextUnitType.Sp).platformScaled())
                }
            }
        )
        NavigationBarItem(
            selected = false, onClick = {
                if (navigator.items.contains(MineScreen)) {
                    navigator.popUntil { it is MineScreen }
                } else {
                    navigator.push(MineScreen)
                }
            }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Filled.Face, contentDescription = null)
                    Text(text = "我的", fontSize = TextUnit(13F, TextUnitType.Sp).platformScaled())
                }
            }
        )
    }
}