package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.api.PictureTools

@Composable
fun Avatar(face: ImageBitmap, pendant: ImageBitmap, width: Dp = 64.dp, height: Dp = 64.dp) {
    Box(modifier = Modifier.width(width).height(height).clip(RoundedCornerShape((width + height) / 4.dp))) {
        Image(modifier = Modifier.fillMaxSize(), bitmap = PictureTools.getMergedAvatar(face, pendant, pendant.height), contentDescription = null)
    }
}