import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "lrk.application.bilibili.client"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))
    // QRCode
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    // Json
    implementation("com.google.code.gson:gson:2.10.1")
    // Network
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    // Gstreamer for VideoPlayer
    implementation("org.freedesktop.gstreamer:gst1-java-core:1.4.0")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    // Navigation
    implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-alpha02")
    // MaterialDesign 3
    implementation("org.jetbrains.compose.material3:material3-desktop:1.2.1")
}

compose.desktop {
    application {
        mainClass = "lrk.application.bilibili.client.Launcher"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.AppImage)
            packageName = "BiliBiliClient"
            packageVersion = "1.0.0"
        }
    }
}
