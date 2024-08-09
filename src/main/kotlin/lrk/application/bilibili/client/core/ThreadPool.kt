package lrk.application.bilibili.client.core

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

val APP_GLOBAL_NETWORK_THREAD_POOL = ThreadPoolExecutor(20, 30, 1, TimeUnit.SECONDS, LinkedBlockingQueue(500))

val APP_GLOBAL_EVENT_THREAD_POOL = ThreadPoolExecutor(5, 10, 2, TimeUnit.SECONDS, LinkedBlockingQueue(50))

val APP_GLOBAL_VIDEO_CACHING_PROCESS_THREAD_POOL = ThreadPoolExecutor(1, 5, 0, TimeUnit.NANOSECONDS, LinkedBlockingQueue(1))