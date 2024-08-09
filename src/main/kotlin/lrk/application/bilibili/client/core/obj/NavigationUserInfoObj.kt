package lrk.application.bilibili.client.core.obj

import com.google.gson.JsonObject
import lrk.application.bilibili.client.core.log.Log

@Suppress("unused")
data class NavigationUserInfoObj(
    val isLogin: Boolean,                           //  是否已登录
    val email_verified: EmailVerifyStatus,          //  是否验证邮箱地址
    val face: String,                               //  用户头像 url
    val level_info: LevelInfo,                      //  等级信息
    val mid: Long,                                   //  用户 mid
    val mobile_verified: MobileVerifyStatus,        //  是否验证手机号
    val money: Int,                                 //  拥有硬币数
    val moral: Int,                                 //  当前节操值
    val official: Official,                         //  认证信息
    val officialVerify: OfficialVerify,             //  认证信息 2
    val pendant: Pendant,                           //  头像框信息
    val scores: Int,                                //  UnKnown
    val uname: String,                              //  用户昵称
    val vipDueDate: Long,                           //  会员到期时间
    val vipStatus: VipStatus,                       //  会员开通状态
    val vipType: VipType,                           //  会员类型
    val vip_pay_type: VipPayType,                   //  会员开通状态
    val vip_theme_type: Int,                        //  UnKnown
    val vip_label: VipLabel,                        //  会员标签
    val vip_avatar_subscript: VipAvatarSubscript,   //  是否显示会员图标
    val vip_nickname_color: String,                 //  会员昵称颜色
    val wallet: Wallet,                             //  B币钱包信息
    val has_shop: Boolean,                          //  是否拥有推广商品
    val shop_url: String,                           //  商品推广页面 url
    val allowance_count: Int,                       //  UnKnown
    val answer_status: Int,                         //  UnKnown
    val is_senior_member: SeniorMemberStatus,       //  是否硬核会员
    val wbi_img: WbiImg,                            //  Wbi 签名实时口令, 该字段即使用户未登录也存在
    val is_jury: Boolean,                           //  是否风纪委员
) {

    companion object {
        fun getInstance(data: JsonObject): NavigationUserInfoObj? {
            val isLogin: Boolean
            val email_verified: EmailVerifyStatus
            val face: String
            val level_info: LevelInfo
            val mid: Long
            val mobile_verified: MobileVerifyStatus
            val money: Int
            val moral: Int
            val official: Official
            val officialVerify: OfficialVerify
            val pendant: Pendant
            val scores: Int
            val uname: String
            val vipDueDate: Long
            val vipStatus: VipStatus
            val vipType: VipType
            val vip_pay_type: VipPayType
            val vip_theme_type: Int
            val vip_label: VipLabel
            val vip_avatar_subscript: VipAvatarSubscript
            val vip_nickname_color: String
            val wallet: Wallet
            val has_shop: Boolean
            val shop_url: String
            val allowance_count: Int
            val answer_status: Int
            val is_senior_member: SeniorMemberStatus
            val wbi_img: WbiImg
            val is_jury: Boolean

            try {
                isLogin = data.get("isLogin").asBoolean
                email_verified = EmailVerifyStatus[data.get("email_verified").asInt]
                face = data.get("face").asString
                level_info = LevelInfo(
                    data.get("level_info").asJsonObject.get("current_level").asInt,
                    data.get("level_info").asJsonObject.get("current_min").asInt,
                    data.get("level_info").asJsonObject.get("current_exp").asInt,
                    data.get("level_info").asJsonObject.get("next_exp").asString,
                )
                mid = data.get("mid").asLong
                mobile_verified = MobileVerifyStatus[data.get("mobile_verified").asInt]
                money = data.get("money").asInt
                moral = data.get("moral").asInt
                official = Official(
                    Official.Role[data.get("official").asJsonObject.get("role").asInt],
                    data.get("official").asJsonObject.get("title").asString,
                    data.get("official").asJsonObject.get("desc").asString,
                    OfficialVerifyStatus[data.get("official").asJsonObject.get("type").asInt]
                )
                officialVerify = OfficialVerify(
                    OfficialVerifyStatus[data.get("officialVerify").asJsonObject.get("type").asInt],
                    data.get("officialVerify").asJsonObject.get("desc").asString
                )
                pendant = Pendant(
                    data.get("pendant").asJsonObject.get("pid").asInt,
                    data.get("pendant").asJsonObject.get("name").asString,
                    data.get("pendant").asJsonObject.get("image").asString,
                    data.get("pendant").asJsonObject.get("expire").asInt
                )
                scores = data.get("scores").asInt
                uname = data.get("uname").asString
                vipDueDate = data.get("vipDueDate").asLong
                vipStatus = VipStatus[data.get("vipStatus").asInt]
                vipType = VipType[data.get("vipType").asInt]
                vip_pay_type = VipPayType[data.get("vip_pay_type").asInt]
                vip_theme_type = data.get("vip_theme_type").asInt
                vip_label = VipLabel(
                    data.get("vip_label").asJsonObject.get("path").asString,
                    data.get("vip_label").asJsonObject.get("text").asString,
                    data.get("vip_label").asJsonObject.get("label_theme").asString,
                )
                vip_avatar_subscript = VipAvatarSubscript[data.get("vip_avatar_subscript").asInt]
                vip_nickname_color = data.get("vip_nickname_color").asString
                wallet = Wallet(
                    data.get("wallet").asJsonObject.get("mid").asLong,
                    data.get("wallet").asJsonObject.get("bcoin_balance").asInt,
                    data.get("wallet").asJsonObject.get("coupon_balance").asInt,
                    data.get("wallet").asJsonObject.get("coupon_due_time").asInt,
                )
                has_shop = data.get("has_shop").asBoolean
                shop_url = data.get("shop_url").asString
                allowance_count = data.get("allowance_count").asInt
                answer_status = data.get("answer_status").asInt
                is_senior_member = SeniorMemberStatus[data.get("is_senior_member").asInt]
                wbi_img = WbiImg(
                    data.get("wbi_img").asJsonObject.get("img_url").asString,
                    data.get("wbi_img").asJsonObject.get("sub_url").asString,
                )
                is_jury = data.get("is_jury").asBoolean

                return NavigationUserInfoObj(
                    isLogin,
                    email_verified,
                    face,
                    level_info,
                    mid,
                    mobile_verified,
                    money,
                    moral,
                    official,
                    officialVerify,
                    pendant,
                    scores,
                    uname,
                    vipDueDate,
                    vipStatus,
                    vipType,
                    vip_pay_type,
                    vip_theme_type,
                    vip_label,
                    vip_avatar_subscript,
                    vip_nickname_color,
                    wallet,
                    has_shop,
                    shop_url,
                    allowance_count,
                    answer_status,
                    is_senior_member,
                    wbi_img,
                    is_jury,
                )
            } catch (e: Exception) {
                Log.e("${this::class.java}: ${e.message}")
                return null
            }
        }
    }


    //  手机号验证状态
    enum class EmailVerifyStatus {
        YES, NO;

        companion object {
            operator fun get(value: Int): EmailVerifyStatus {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }

    data class LevelInfo(
        val current_level: Int, //  当前等级
        val current_min: Int,   //  当前等级经验最低值
        val current_exp: Int,   //  当前经验
        val next_exp: String,      //  升级下一等级需达到的经验, 当用户等级为Lv6时，值为--，代表无穷大; 小于6级时：num 6级时：str
    )

    data class Official(
        val role: Role,     //  认证类型
        val title: String,  //  认证信息
        val desc: String,   //  认证备注
        val type: OfficialVerifyStatus,      //  是否认证, -1：无, 0: 认证
    ) {
        enum class Role {
            NONE,               //  无
            UP,                 //  知名UP主
            V,                  //  大V达人
            COMPANY,            //  企业
            ORGANIZATION,       //  组织
            MEDIA,              //  媒体
            GOVERNMENT,         //  政府
            HIGH_ENERGY_ANCHOR, //  高能主播
            SOCIAL_CELEBRITIES; //  社会知名人士

            companion object {
                operator fun get(type: Int): Role {
                    return Role.entries[type]
                }
            }
        }
    }

    data class OfficialVerify(
        val type: OfficialVerifyStatus, //  是否认证, -1：无, 0: 认证
        val desc: String,               //  认证信息
    )

    //  账号官方认证状态
    enum class OfficialVerifyStatus {
        YES, NO;

        companion object {
            operator fun get(value: Int): OfficialVerifyStatus {
                return when (value) {
                    0 -> YES
                    1 -> NO
                    else -> NO
                }
            }
        }
    }

    //  手机号验证状态
    enum class MobileVerifyStatus {
        YES, NO;

        companion object {
            operator fun get(value: Int): MobileVerifyStatus {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }

    //  头像框信息
    data class Pendant(
        val pid: Int,       //  挂件id
        val name: String,   //  挂件名称
        val image: String,  //  挂件图片url
        val expire: Int,    //  可能是挂件到期时间
    )

    //  会员开通状态
    enum class VipStatus {
        YES, NO;

        companion object {
            operator fun get(value: Int): VipStatus {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }

    //  会员类型
    enum class VipType {
        NONE,                   //  无
        MONTH_VIP,              //  月度大会员
        ANNUAL_VIP_AND_ABOVE;   //  年度及以上大会员

        companion object {
            operator fun get(value: Int): VipType {
                return VipType.entries[value]
            }
        }
    }

    //  会员开通状态
    enum class VipPayType {
        YES, NO;

        companion object {
            operator fun get(value: Int): VipPayType {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }

    //  是否显示会员图标
    enum class VipAvatarSubscript {
        YES, NO;

        companion object {
            operator fun get(value: Int): VipAvatarSubscript {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }

    // 会员类型
    data class VipLabel(
        val path: String,           //  ?
        val text: String,           //  会员名称
        val label_theme: String,    //  会员标签
    )

    // 钱包信息
    data class Wallet(
        val mid: Long,               //  登录用户mid
        val bcoin_balance: Int,     //  拥有B币数
        val coupon_balance: Int,    //  每月奖励B币数
        val coupon_due_time: Int,   //  ?
    )

    data class WbiImg(
        val img_url: String,    //  Wbi 签名参数 imgKey的伪装 url
        val sub_url: String,    //  Wbi 签名参数 subKey的伪装 url
    )


    enum class SeniorMemberStatus {
        YES, NO;

        companion object {
            operator fun get(value: Int): SeniorMemberStatus {
                return when (value) {
                    1 -> YES
                    0 -> NO
                    else -> NO
                }
            }
        }
    }
}

