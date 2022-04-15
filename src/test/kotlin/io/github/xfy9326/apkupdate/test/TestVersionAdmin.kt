package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.VersionManager
import io.github.xfy9326.apkupdate.server.plugins.GeneralJson
import io.github.xfy9326.apkupdate.utils.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVersionAdmin {
    @BeforeTest
    fun beforeTest() {
        withTestServer {
            assertEquals(OperationStatus.SUCCESS, ProjectManager.createProject(TEST_PROJECT))
        }
    }

    @AfterTest
    fun afterTest() {
        withTestServer {
            assertEquals(OperationStatus.SUCCESS, ProjectManager.deleteProject(TEST_PROJECT))
        }
    }

    @Test
    fun testVersionAdd() {
        withTestServer {
            val authProvider = getAuthProvider()
            val requestCall: HttpRequestBuilder.() -> Unit = {
                setBody(GeneralJson.encodeToString(TEST_VERSION))
                headers.append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            it.authRequest(authProvider, HttpMethod.Put, "/$TEST_PROJECT/$TEST_CHANNEL", requestCall).apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(hasVersion(TEST_PROJECT, TEST_VERSION.versionCode))
            assert(hasDownloadSource(TEST_PROJECT, TEST_CHANNEL))
            it.authRequest(authProvider, HttpMethod.Put, "/$TEST_PROJECT/$TEST_CHANNEL", requestCall).apply {
                assertEquals(OperationStatus.EXISTS.statusCode, status.value)
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersionDelete() {
        withTestServer {
            val authProvider = getAuthProvider()
            assertEquals(OperationStatus.SUCCESS, VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION))
            it.authRequest(authProvider, HttpMethod.Delete, "/$TEST_PROJECT/$TEST_CHANNEL?version=${TEST_VERSION.versionCode}").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(!hasVersion(TEST_PROJECT, TEST_VERSION.versionCode))
            assert(!hasDownloadSource(TEST_PROJECT, TEST_CHANNEL))
            it.authRequest(authProvider, HttpMethod.Delete, "/$TEST_PROJECT/$TEST_CHANNEL?version=${TEST_VERSION.versionCode}").apply {
                assertEquals(OperationStatus.NOT_EXISTS.statusCode, status.value)
            }
        }
    }
}