package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.core.cleanVideoCache

@Composable
fun HomePageTopBar(height: Dp = 50.dp) {
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.height(height.platformScaled()),
        title = {
            Text(text = "BiliBiliClient", fontSize = TextUnit(18F, TextUnitType.Sp).platformScaled())
        },
        actions = {

        },
    )
}

@Composable
fun VideoPlayerPageTopBar(navigator: Navigator?, title: String){
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.background,
        navigationIcon = {
            IconButton(onClick = {
                cleanVideoCache()
                navigator?.pop()
            }){
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(text = title, fontSize = TextUnit(15F, TextUnitType.Sp).platformScaled())
        },
        actions = {

        },
    )
}