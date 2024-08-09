package lrk.application.bilibili.client.api

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import org.jetbrains.skiko.toBitmap
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.net.URI
import javax.imageio.ImageIO

fun BilibiliApi.getPicture(
    url: String,
    width: Int = 256,
    height: Int = 256,
    quality: Int = 100,
    format: String = "png"
): ImageBitmap? {
    if (url.isEmpty()) return null
    val pictureURI = URI("$url@${width}w_${height}h_${quality}q.$format")
    return ImageIO.read(pictureURI.toURL()).toBitmap().asComposeImageBitmap()
}

object PictureTools{

    fun getEmptyImageBitmap(width: Int, height: Int): ImageBitmap {
        return ImageBitmap(width, height, ImageBitmapConfig.Argb8888, true, ColorSpaces.Srgb)
    }

    private fun getScaledImageBitmap(source: ImageBitmap, scale: Float): ImageBitmap {
        val sourceBufferedImage = getCircleImage(source.toAwtImage())
        val resultBufferedImage = BufferedImage(
            (sourceBufferedImage.width * scale).toInt(),
            (sourceBufferedImage.height * scale).toInt(),
            BufferedImage.TYPE_INT_ARGB
        )
        resultBufferedImage.graphics.drawImage(
            sourceBufferedImage, 0, 0,
            (sourceBufferedImage.width * scale).toInt(),
            (sourceBufferedImage.height * scale).toInt(),
            null
        )
        return resultBufferedImage.toBitmap().asComposeImageBitmap()
    }

    private fun getCircleImage(rawImage: BufferedImage): BufferedImage {
        val width = rawImage.width
        val image = BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB)
        val g = image.graphics as Graphics2D
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        val shape = Ellipse2D.Double(0.0, 0.0, width.toDouble(), width.toDouble())
        g.clip = shape
        g.drawImage(rawImage, 0, 0, width, width, null)
        return image
    }

    fun getMergedAvatar(face: ImageBitmap, pendant: ImageBitmap, imageSize: Int): ImageBitmap {
        val bitmap = getEmptyImageBitmap(imageSize, imageSize)
        val faceScaled = getScaledImageBitmap(face, 0.6F)
        val canvas = Canvas(bitmap)
        canvas.drawImage(faceScaled, Offset(imageSize / 5F, imageSize / 5F), Paint())
        canvas.drawImage(pendant, Offset(0F, 0F), Paint())
        canvas.save()
        canvas.restore()
        return bitmap
    }
}
