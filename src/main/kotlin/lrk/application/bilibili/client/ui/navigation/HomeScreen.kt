package lrk.application.bilibili.client.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import lrk.application.bilibili.client.ui.components.pages.HomePage

object HomeScreen : Screen {
    private fun readResolve(): Any = HomeScreen

    @Composable
    override fun Content() {
        HomePage()
    }

}