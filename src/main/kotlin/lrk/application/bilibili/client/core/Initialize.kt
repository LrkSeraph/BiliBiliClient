package lrk.application.bilibili.client.core

import com.google.gson.Gson
import com.sun.jna.platform.win32.Kernel32
import lrk.application.bilibili.client.Platform
import lrk.application.bilibili.client.core.log.Log
import org.freedesktop.gstreamer.Gst
import org.freedesktop.gstreamer.Version
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.Toolkit
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI
import java.util.*
import java.util.stream.Stream
import java.util.zip.ZipInputStream
import javax.swing.*

object Initialize {

    private const val GSTREAMER_DOWNLOAD_URL =
        "https://gstreamer.freedesktop.org/data/pkg/windows/1.22.10/msvc/gstreamer-1.0-msvc-x86_64-1.22.10.msi"

    fun initialize() {
        if (Platform.getPlatform() == Platform.Windows) {
            GStreamerUtils.configurePaths()
            deployGStreamerOnWindows()
        }
        loadCookie()
        Gst.init(Version.BASELINE, "BiliBiliClient")
    }

    private fun loadCookie() {
        if (!AppConfig.cookieFile.exists()) {
            AppConfig.cookieFile.createNewFile()
            AppConfig.cookie = mutableMapOf()
        }
        FileInputStream(AppConfig.cookieFile).use {
            val cookieData = String(it.readAllBytes())
            AppConfig.cookie = Gson().fromJson(cookieData, AppConfig.cookie::class.java) ?: mutableMapOf()
        }
    }

    private fun deployGStreamerOnWindows() {
        val targetDir = File(Platform.getPlatformDataDir().path + File.separator + "gstreamer")
        if (targetDir.exists() && targetDir.isDirectory && (targetDir.listFiles()?.size ?: 0) != 0) return
        if (Platform.getPlatform() == Platform.Windows) {
            val jFrame = JFrame("Install GStreamer")
            val jLabel = JLabel("需要安装Gstreamer以提供视频播放功能")
            jLabel.horizontalAlignment = SwingConstants.CENTER
            val jPanel = JPanel().also { jPanel ->
                jPanel.add(
                    JButton("Install").also {
                        it.addActionListener {
                            APP_GLOBAL_EVENT_THREAD_POOL.execute {
                                jLabel.text = "Downloading..."
                                jPanel.isVisible = false
                                jFrame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
                                val msiLocation =
                                    File(Platform.getPlatformDataDir().path + File.separator + "gstreamer-1.0-msvc-x86_64-1.22.10.msi")
                                FileOutputStream(msiLocation).use { fileOutputStream ->
                                    fileOutputStream.write(
                                        URI(GSTREAMER_DOWNLOAD_URL).toURL().openStream().readAllBytes()
                                    )
                                }
                                Runtime.getRuntime()
                                    .exec(
                                        arrayOf(
                                            "msiexec",
                                            "/passive",
                                            "INSTALLDIR=${targetDir.path}",
                                            "/i",
                                            msiLocation.path
                                        ),
                                        arrayOf(),
                                        File(
                                            Platform.getPlatformDataDir().path
                                        )
                                    )
                            }
                        }
                    }
                )
            }
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            jFrame.setLocation((screenSize.width - 400) / 2, (screenSize.height - 220) / 2)
            jFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            jFrame.layout = GridLayout(2, 1)
            jFrame.add(jLabel)
            jFrame.add(jPanel)
            jFrame.size = Dimension(400, 220)
            jFrame.isResizable = false
            jFrame.isVisible = true
            while (!targetDir.exists()) {
                Thread.sleep(1000)
            }
            jFrame.dispose()
        }
    }

    object GStreamerUtils {
        fun configurePaths() {
            if (com.sun.jna.Platform.isWindows()) {
                val gstPath = System.getProperty("gstreamer.path", findWindowsLocation())
                if (gstPath.isNotEmpty()) {
                    val systemPath = System.getenv("PATH")
                    if (systemPath == null || systemPath.trim().isEmpty()) {
                        Kernel32.INSTANCE.SetEnvironmentVariable("PATH", gstPath)
                    } else {
                        Kernel32.INSTANCE.SetEnvironmentVariable(
                            "PATH", gstPath + File.pathSeparator + systemPath
                        )
                    }
                }
            } else if (com.sun.jna.Platform.isMac()) {
                val gstPath = System.getProperty(
                    "gstreamer.path",
                    "/Library/Frameworks/GStreamer.framework/Libraries/"
                )
                if (gstPath.isNotEmpty()) {
                    val jnaPath = System.getProperty("jna.library.path", "").trim()
                    if (jnaPath.isEmpty()) {
                        System.setProperty("jna.library.path", gstPath)
                    } else {
                        System.setProperty("jna.library.path", jnaPath + File.pathSeparator + gstPath)
                    }
                }
            }
        }

        private fun findWindowsLocation(): String {
            return if (com.sun.jna.Platform.is64Bit()) {
                Stream.of(
                    "GSTREAMER_1_0_ROOT_MSVC_X86_64",
                    "GSTREAMER_1_0_ROOT_MINGW_X86_64",
                    "GSTREAMER_1_0_ROOT_X86_64"
                )
                    .map { System.getenv(it) }
                    .filter { Objects.nonNull(it) }
                    .map { p: String ->
                        if (p.endsWith(
                                "\\"
                            )
                        ) "${p}bin\\" else "$p\\bin\\"
                    }
                    .findFirst().orElse("")
            } else {
                ""
            }
        }
    }
}
