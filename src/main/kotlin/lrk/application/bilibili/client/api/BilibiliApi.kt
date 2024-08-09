package lrk.application.bilibili.client.api

@Suppress("unused")
object BilibiliApi {
    // request a login qrcode
    const val API_QRCODE_LOGIN_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate"

    // get the qrcode scan state
    const val API_QRCODE_CHECK_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll"

    // gat navigation user information
    const val API_USER_NAVIGATION_INFORMATION_URL = "https://api.bilibili.com/x/web-interface/nav"

    // get user space information
    const val API_USER_SPACE_INFORMATION_URL = "https://api.bilibili.com/x/space/wbi/acc/info"

    // check if the cookie should to be refreshed
    const val API_COOKIE_REFRESH_CHECK_URL = "https://passport.bilibili.com/x/passport-login/web/cookie/info"

    // get some recommend videos
    const val API_GET_RECOMMEND_VIDEOS_URL = "https://api.bilibili.com/x/web-interface/index/top/rcmd"

    // get the address of video
    const val API_GET_VIDEO_ADDRESS_URL = "https://api.bilibili.com/x/player/wbi/playurl"

    // the api for search
    const val API_SEARCH = "https://api.bilibili.com/x/web-interface/wbi/search/all/v2"

    // 大会员积分签到
    const val API_VIP_SIGN = "https://api.bilibili.com/pgc/activity/score/task/sign"
}