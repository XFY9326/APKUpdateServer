package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.utils.withTestServer
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals

class TestServer {
    @Test
    fun testServerConnection() {
        withTestServer {
            it.head("/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }
    }
}