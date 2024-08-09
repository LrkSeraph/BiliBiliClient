package lrk.application.bilibili.client.core.obj

import com.google.gson.JsonObject

@Suppress("unused")
data class RecommendVideoInfoObj(
    val id: Int,
    val bvid: String,
    val cid: Int,
    val goto: String,
    val uri: String,
    val pic: String,
    val title: String,
    val duration: Int,
    val pubdate: Int,
    val owner: Owner,
    val stat: Stat,
    val av_feature: String?,
    val is_followed: Int,
    val rcmd_reason: RecommendReason?,
//    val show_info: Int,
//    val track_id: String,
//    val pos: Int,
//    val room_info: Any?,
//    val ogv_info: Any?,
//    val business_info: Any?,
//    val is_stock: Int,
//    val enable_vt: Int,
//    val vt_display: Int,
//    val dislike_switch: Int,
) {

    companion object {
        fun getInstance(data: JsonObject): RecommendVideoInfoObj {
            val id: Int
            val bvid: String
            val cid: Int
            val goto: String
            val uri: String
            val pic: String
            val title: String
            val duration: Int
            val pubdate: Int
            val owner: Owner
            val stat: Stat
            val av_feature: String?
            val is_followed: Int
            val rcmd_reason: RecommendReason?

            id = data.get("id").asInt
            bvid = data.get("bvid").asString
            cid = data.get("cid").asInt
            goto = data.get("goto").asString
            uri = data.get("uri").asString
            pic = data.get("pic").asString
            title = data.get("title").asString
            duration = data.get("duration").asInt
            pubdate = data.get("pubdate").asInt
            owner = Owner(
                data.get("owner").asJsonObject.get("mid").asLong,
                data.get("owner").asJsonObject.get("name").asString,
                data.get("owner").asJsonObject.get("face").asString,
            )
            stat = Stat(
                data.get("stat").asJsonObject.get("view").asInt,
                data.get("stat").asJsonObject.get("like").asInt,
                data.get("stat").asJsonObject.get("danmaku").asInt,
                data.get("stat").asJsonObject.get("vt").asInt,
            )
            av_feature = if (data.get("av_feature").isJsonNull) null else data.get("av_feature").asString
            is_followed = data.get("is_followed").asInt
            rcmd_reason =
                RecommendReason(
                    if (data.has("rcmd_reason") &&
                        !data.get("rcmd_reason").isJsonNull &&
                        data.get("rcmd_reason").asJsonObject.has("content") &&
                        !data.get("rcmd_reason").asJsonObject.get("content").isJsonNull
                    ) {
                        data.get("rcmd_reason").asJsonObject.get("content").asString
                    } else
                        null,
                    if (data.has("rcmd_reason") &&
                        !data.get("rcmd_reason").isJsonNull
                    )
                        data.get("rcmd_reason").asJsonObject.get("reason_type").asInt
                    else
                        0,
                )

            return RecommendVideoInfoObj(
                id,
                bvid,
                cid,
                goto,
                uri,
                pic,
                title,
                duration,
                pubdate,
                owner,
                stat,
                av_feature,
                is_followed,
                rcmd_reason,
            )
        }
    }



    data class Owner(
        val mid: Long,
        val name: String,
        val face: String,
    )

    data class Stat(
        val view: Int,
        val like: Int,
        val danmaku: Int,
        val vt: Int,
    )

    data class RecommendReason(
        val content: String?,
        val reason_type: Int,
    )

    // use bvid and cid to identify a video

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecommendVideoInfoObj

        if (bvid != other.bvid) return false
        if (cid != other.cid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bvid.hashCode()
        result = 31 * result + cid
        return result
    }


}