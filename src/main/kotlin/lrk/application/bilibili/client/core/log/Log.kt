package lrk.application.bilibili.client.core.log

import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.AppState

object Log {
    fun i(message: String) {
        val logString = "[INFO]\t$message"
        AppState.LogState.logQueue.offer(logString)
        AppState.LogState.logEntries.value++
        println(logString)
    }

    fun d(message: String) {
        if (AppConfig.DEBUG) {
            val logString = "[DEBUG]\t$message"
            AppState.LogState.logQueue.offer(logString)
            AppState.LogState.logEntries.value++
            println(logString)
        }
    }

    fun e(message: String) {
        val logString = "[ERROR]\t$message"
        AppState.LogState.logQueue.offer(logString)
        AppState.LogState.logEntries.value++
        System.err.println(logString)
    }

    fun clear(){
        AppState.LogState.logQueue.clear()
        AppState.LogState.logEntries.value = 0
    }
}
