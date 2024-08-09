package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.log.Log

@Composable
fun LogInfoBlock() {
    LazyColumn(modifier = Modifier.padding(start = 5.dp)) {
        items(AppState.LogState.logEntries.value) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                // unknown reason for exception, just catch it to keep application alive
                text = try {
                    AppState.LogState.logQueue[it].replace("\t", "  ")
                } catch (e: Exception) {
                    e.message?.let { it1 -> Log.e(it1) }
                    ""
                },
                fontSize = TextUnit(12F, TextUnitType.Sp).platformScaled()
            )
        }
    }
}