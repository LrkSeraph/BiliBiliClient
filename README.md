# BiliBiliClient
## What is this?
A simple bilibili client based on compose multiplatform framework, it use api from [bilibili-API-collect](https://github.com/SocialSisterYi/bilibili-API-collect) <br>
You can use it to watch video from [BiliBili](https://www.bilibili.com)
## Getting Start
No release will be provided because this project is in developing. <br>
But you can clone this project and build it. <br>
```bash
git clone https://github.com/Seraph-new/BiliBiliClient.git
```
Build it with ./gradlew
```bash
./gradlew packageDistributionForCurrentOS
```
Then you can found AppImage in directory build/compose/binaries/main/app/ <br>
Or just use `./gradlew run` to build and run this program <br>
## About VideoPlayer
This project is using experimental VideoPlayer component from [JetBrains Compose Multiplatform](https://www.jetbrains.com/zh-cn/lp/compose-multiplatform/). <br>
It can be found at [here](https://github.com/JetBrains/compose-multiplatform/tree/master/experimental/components). <br>
But it has some problem because it use vlcj in SwingPanel: [Issue 1521](https://github.com/JetBrains/compose-multiplatform/issues/1521), [Issue 2926](https://github.com/JetBrains/compose-multiplatform/issues/2926). <br>
So this project is trying to rewrite a new VideoPlayer with gstreamer. <br>
