package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.VersionManager
import io.github.xfy9326.apkupdate.server.plugins.GeneralJson
import io.github.xfy9326.apkupdate.utils.*
import io.ktor.http.*
import io.ktor.server.testing.*
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
            val requestCall: TestApplicationRequest.() -> Unit = {
                setBody(GeneralJson.encodeToString(TEST_VERSION))
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            handleAuthRequest(authProvider, HttpMethod.Put, "/$TEST_PROJECT/$TEST_CHANNEL", requestCall).apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, response.status()?.value)
            }
            assert(hasVersion(TEST_PROJECT, TEST_VERSION.versionCode))
            assert(hasDownloadSource(TEST_PROJECT, TEST_CHANNEL))
            handleAuthRequest(authProvider, HttpMethod.Put, "/$TEST_PROJECT/$TEST_CHANNEL", requestCall).apply {
                assertEquals(OperationStatus.EXISTS.statusCode, response.status()?.value)
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersionDelete() {
        withTestServer {
            val authProvider = getAuthProvider()
            assertEquals(OperationStatus.SUCCESS, VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION))
            handleAuthRequest(authProvider, HttpMethod.Delete, "/$TEST_PROJECT/$TEST_CHANNEL?version=${TEST_VERSION.versionCode}").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, response.status()?.value)
            }
            assert(!hasVersion(TEST_PROJECT, TEST_VERSION.versionCode))
            assert(!hasDownloadSource(TEST_PROJECT, TEST_CHANNEL))
            handleAuthRequest(authProvider, HttpMethod.Delete, "/$TEST_PROJECT/$TEST_CHANNEL?version=${TEST_VERSION.versionCode}").apply {
                assertEquals(OperationStatus.NOT_EXISTS.statusCode, response.status()?.value)
            }
        }
    }
}