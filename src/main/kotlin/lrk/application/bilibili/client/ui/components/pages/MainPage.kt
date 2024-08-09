package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import lrk.application.bilibili.client.ui.theme.AppTheme
import lrk.application.bilibili.client.ui.navigation.HomeScreen

@Composable
fun MainPage() {
    AppTheme {
        Navigator(HomeScreen)
    }
}