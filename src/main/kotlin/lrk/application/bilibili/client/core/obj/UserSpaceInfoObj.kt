package lrk.application.bilibili.client.core.obj

import com.google.gson.JsonObject
import lrk.application.bilibili.client.core.log.Log

@Suppress("unused")
data class UserSpaceInfoObj(
    val mid: Long,
    val name: String,
    val sex: String,
    val face: String,
    val face_nft: Boolean,
    val sign: String,
    val level: Int,
    val silence: Boolean,
    // TODO: add other fields
){
    companion object{
        fun getInstance(data: JsonObject): UserSpaceInfoObj?{
            val mid: Long
            val name: String
            val sex: String
            val face: String
            val face_nft: Boolean
            val sign: String
            val level: Int
            val silence: Boolean

            try {
                mid = data.get("mid").asLong
                name = data.get("name").asString
                sex = data.get("sex").asString
                face = data.get("face").asString
                face_nft = data.get("face_nft").asInt == 1
                sign = data.get("sign").asString
                level = data.get("level").asInt
                silence = data.get("silence").asInt == 1

                return UserSpaceInfoObj(mid, name, sex, face, face_nft, sign, level, silence)
            }catch (e: Exception){
                Log.e("${this::class.java}: ${e.message}")
                return null
            }
        }
    }
}
