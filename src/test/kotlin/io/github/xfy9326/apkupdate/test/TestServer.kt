package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.utils.withTestServer
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class TestServer {
    @Test
    fun testServerConnection() {
        withTestServer {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}