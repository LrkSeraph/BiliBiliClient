package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.jetbrains.skiko.toBitmap
import java.util.*

@Composable
fun QRCode(data: MutableState<String>, qrcodeSize: Int = 150) {
    val hints: MutableMap<EncodeHintType, Any> = EnumMap(com.google.zxing.EncodeHintType::class.java)
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
    hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
    hints[EncodeHintType.MARGIN] = 3

    Image(
        bitmap =
        MatrixToImageWriter.toBufferedImage(
            MultiFormatWriter().encode(
                data.value, BarcodeFormat.QR_CODE,
                qrcodeSize, qrcodeSize, hints
            )
        ).toBitmap().asComposeImageBitmap(),
        contentDescription = data.value,
    )

}

@Composable
fun QRCodeWaitForScan(qrcodeData: MutableState<String>, text: MutableState<String>, qrcodeSize: Int = 150) {
    Surface(
        color = MaterialTheme.colors.surface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, Color.Black),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            QRCode(qrcodeData, qrcodeSize)
            Text(
                modifier = Modifier.fillMaxWidth().padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                text = text.value,
                textAlign = TextAlign.Center,
            )
        }
    }
}