package lrk.application.bilibili.client.core

import org.junit.Test

import org.junit.Assert.*

class RequestKtTest {

    @Test
    fun makeGetURLWithParamsTest() {
        assertEquals("https://passport.bilibili.com/x/passport-login/web/cookie/info?csrf=1696a2cd9ab60336bb47506df6c2af&sid=666", makeGetURL(
            AppConfig.API_COOKIE_REFRESH_CHECK_URL, "csrf" to "1696a2cd9ab60336bb47506df6c2af", "sid" to "666"))
    }
}