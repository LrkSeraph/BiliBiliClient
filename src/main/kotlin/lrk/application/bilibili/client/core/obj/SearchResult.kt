package lrk.application.bilibili.client.core.obj

sealed class SearchResult private constructor() {
    data class Video(
        val type: String,
        val id: Int,
        val author: String,
        val mid: Long,
        // TODO: add other fields
    )
}