package lrk.application.bilibili.client.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import lrk.application.bilibili.client.ui.components.pages.MinePage

object MineScreen : Screen {
    private fun readResolve(): Any = MineScreen

    @Composable
    override fun Content() {
        MinePage()
    }
}