package lrk.application.bilibili.client

import androidx.compose.material.Text
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import lrk.application.bilibili.client.core.AppConfig
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


enum class Platform {
    Windows,
    UnixLike;

    companion object {
        fun getPlatform(): Platform {
            return if (System.getProperty("os.name").lowercase().contains("windows")) {
                Windows
            } else {
                UnixLike
            }
        }

        fun getPlatformCharset(): Charset {
            return when (getPlatform()) {
                Windows -> Charset.forName("GBK")
                UnixLike -> StandardCharsets.UTF_8
            }
        }

        fun getPlatformDataDir(): File {
            return when (getPlatform()) {
                Windows -> {
                    val file = File("${System.getenv("LOCALAPPDATA")}\\${AppConfig.APP_NAME}")
                    if (file.isFile) file.delete()
                    if (!file.exists()) file.mkdirs()
                    file
                }

                UnixLike -> {
                    val file = File("${System.getProperty("user.home")}/.config/${AppConfig.APP_NAME}")
                    if (file.isFile) file.delete()
                    if (!file.exists()) file.mkdirs()
                    file
                }
            }
        }

        fun Dp.platformScaled(): Dp {
            return if (getPlatform() == UnixLike) {
                this.times(1.5f)
            } else {
                this
            }
        }

        fun TextUnit.platformScaled(): TextUnit {
            return if (getPlatform() == UnixLike) {
                return TextUnit(this.value * 1.5f, this.type)
            } else {
                this
            }
        }
    }
}
