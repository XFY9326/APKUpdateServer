package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.VersionIndex
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.VersionManager
import io.github.xfy9326.apkupdate.server.plugins.GeneralJson
import io.github.xfy9326.apkupdate.utils.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.decodeFromString
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVersion {

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

    private inline fun <reified T> TestApplicationCall.receive(): T? =
        response.content?.let { GeneralJson.decodeFromString<T>(it) }

    @Test
    fun testLatestVersion() {
        withTestServer {
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/$TEST_CHANNEL/latest").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION))
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/$TEST_CHANNEL/latest").apply {
                assertEquals(TEST_VERSION, receive())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersion() {
        withTestServer {
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/version/${TEST_VERSION.versionCode}").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/version/${TEST_VERSION.versionCode}").apply {
                assertEquals(TEST_VERSION, receive())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersionList() {
        withTestServer {
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/version").apply {
                assertEquals(emptyList<VersionIndex>(), receive())
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/version").apply {
                assertEquals(TEST_VERSION_INDEX_LIST, receive())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testLatestDownload() {
        withTestServer {
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/$TEST_CHANNEL").apply {
                assertEquals(HttpStatusCode.NotFound, response.status())
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            handleRequest(HttpMethod.Get, "/$TEST_PROJECT/$TEST_CHANNEL").apply {
                assertEquals(TEST_VERSION.downloadSources[0].url, response.headers[HttpHeaders.Location])
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }
}