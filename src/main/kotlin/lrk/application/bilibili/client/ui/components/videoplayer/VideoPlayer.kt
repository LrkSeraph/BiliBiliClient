package lrk.application.bilibili.client.ui.components.videoplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Dp
import org.freedesktop.gstreamer.Caps
import org.freedesktop.gstreamer.FlowReturn
import org.freedesktop.gstreamer.Sample
import org.freedesktop.gstreamer.elements.AppSink
import org.freedesktop.gstreamer.elements.AppSink.NEW_PREROLL
import org.freedesktop.gstreamer.elements.AppSink.NEW_SAMPLE
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.awt.image.DataBufferInt
import java.nio.ByteOrder
import java.nio.IntBuffer

fun getAppSink(name: String): AppSink = AppSink(name).also {
    it.set("emit-signals", true)
    it.caps =
        Caps("video/x-raw,pixel-aspect-ratio=1/1,format=${if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) "BGRx" else "xRGB"}")
}

class AppSinkListener(
    private val renderImageBitmap: MutableState<ImageBitmap>,
    private val videoScaleWidth: MutableState<Int>,
    private val videoScaleHeight: MutableState<Int>,
) : NEW_SAMPLE, NEW_PREROLL {

    private var currentImage: BufferedImage? = null
    private var updating = false

    override fun newSample(elem: AppSink): FlowReturn {
        val sample: Sample = elem.pullSample()
        val capsStruct = sample.caps.getStructure(0)
        val width = capsStruct.getInteger("width")
        val height = capsStruct.getInteger("height")
        val buffer = sample.buffer
        buffer.map(false)?.let {
            rgbFrame(false, width, height, it.asIntBuffer())
            buffer.unmap()
        }
        sample.dispose()
        return FlowReturn.OK
    }

    override fun newPreroll(elem: AppSink): FlowReturn {
        val sample: Sample = elem.pullPreroll()
        val capsStruct = sample.caps.getStructure(0)
        val width = capsStruct.getInteger("width")
        val height = capsStruct.getInteger("height")
        val buffer = sample.buffer
        buffer.map(false)?.let {
            rgbFrame(true, width, height, it.asIntBuffer())
            buffer.unmap()
        }
        sample.dispose()
        return FlowReturn.OK
    }

    private fun getBufferedImage(width: Int, height: Int): BufferedImage {
        if (currentImage != null && currentImage!!.width == width && currentImage!!.height == height) {
            return currentImage as BufferedImage
        }
        currentImage?.flush()
        currentImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB).apply {
            accelerationPriority = 0.0f
        }
        return currentImage as BufferedImage
    }

    private fun scaledBufferedImage(bufferedImage: BufferedImage): BufferedImage {
        val scaledBufferedImage = BufferedImage(videoScaleWidth.value, videoScaleHeight.value, BufferedImage.TYPE_INT_RGB)
        with(scaledBufferedImage.graphics as Graphics2D){
            setRenderingHints(mapOf(RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON))
            drawImage(bufferedImage, 0, 0, videoScaleWidth.value, videoScaleHeight.value, null)
            dispose()
        }
        return scaledBufferedImage
    }

    private fun rgbFrame(isPreRoll: Boolean, width: Int, height: Int, rgb: IntBuffer) {
        if (!isPreRoll && updating) return
        val renderImage: BufferedImage = getBufferedImage(width, height)
        val pixels = (renderImage.raster.dataBuffer as DataBufferInt).data
        rgb[pixels, 0, width * height]
        updating = true
        renderImageBitmap.value = scaledBufferedImage(renderImage).toComposeImageBitmap()
        updating = false
        renderImage.flush()
    }
}

@Composable
fun VideoPlayer(appSink: AppSink, width: Dp, height: Dp) {
    val imageBitmap = remember {
        mutableStateOf(
            ImageBitmap(
                width.value.toInt(),
                height.value.toInt(),
                ImageBitmapConfig.Argb8888,
                true,
                ColorSpaces.Srgb
            )
        )
    }
    val videoScaleWidth = remember { mutableStateOf(width.value.toInt()) }
    val videoScaleHeight = remember { mutableStateOf(height.value.toInt()) }
    val appSinkListener = remember { AppSinkListener(imageBitmap, videoScaleWidth, videoScaleHeight) }

    Box(modifier = Modifier.width(width).height(height).onSizeChanged { intSize ->
        videoScaleWidth.value = intSize.width
        videoScaleHeight.value = intSize.height
    }) {
        Image(modifier = Modifier.fillMaxSize(), bitmap = imageBitmap.value, contentDescription = null)
    }
    LaunchedEffect(Unit) {
        appSink.connect(appSinkListener as NEW_SAMPLE)
        appSink.connect(appSinkListener as NEW_PREROLL)
    }
}