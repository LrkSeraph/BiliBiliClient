package lrk.application.bilibili.client.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import java.io.InputStream
import java.util.*

object AppState {
    object LoginState {
        var qrcodeKey: String = ""
        var shouldReLogin: MutableState<Boolean> = mutableStateOf(false)
    }
    object LogState{
        val logQueue: LinkedList<String> = LinkedList()
        var logEntries: MutableState<Int> = mutableStateOf(0)
    }
    object RecommendVideoPoolState{
        val recommendVideoPool: RecommendVideoPool = RecommendVideoPool()
        var recommendVideoPoolSize: MutableState<Int> = mutableStateOf(0)
        var currentScrollIndex: MutableState<Int> = mutableStateOf(0)
    }
    object VideoProcessState{
        var CURRENT_VIDEO_CACHING_PROCESS_THREAD: Thread? = null
        var SERVER_PREPARED: Boolean = false
        var CURRENT_SOURCE_STREAM: InputStream? = null
    }
}

class RecommendVideoPool{
    private val map = HashMap<Int, RecommendVideoInfoObj>()
    private var currentIndex = 0
    val size: Int
        get() = map.size

    private fun add(value: RecommendVideoInfoObj){
        map[currentIndex++] = value
    }

    fun addAll(values: Collection<RecommendVideoInfoObj>){
        values.forEach(::add)
    }

    operator fun get(index: Int): RecommendVideoInfoObj?{
        return map[index]
    }

}