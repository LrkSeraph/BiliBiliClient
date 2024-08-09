package lrk.application.bilibili.client.core

import org.junit.Assert.assertEquals
import org.junit.Test

class AlgorithmTest {

    @Test
    fun signedQueryString() {
        val param = mutableMapOf(
            "id" to "123456",
            "str" to "654321",
            "test" to "nullptr"
        )
        assertEquals(
            "appkey=1d8b6e7d45233436&id=123456&str=654321&test=nullptr&sign=1ed9e37a0cc04e7a0d7b1e035f146091",
            Algorithm.signedQueryString(param)
        )
    }

}