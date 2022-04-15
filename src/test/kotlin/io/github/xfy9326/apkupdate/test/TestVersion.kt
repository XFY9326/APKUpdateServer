package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.VersionIndex
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.VersionManager
import io.github.xfy9326.apkupdate.utils.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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

    @Test
    fun testLatestVersion() {
        withTestServer {
            it.get("/$TEST_PROJECT/$TEST_CHANNEL/latest").apply {
                assertEquals(HttpStatusCode.NotFound, status)
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION))
            it.get("/$TEST_PROJECT/$TEST_CHANNEL/latest").apply {
                assertEquals(TEST_VERSION, body())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersion() {
        withTestServer {
            it.get("/$TEST_PROJECT/version/${TEST_VERSION.versionCode}").apply {
                assertEquals(HttpStatusCode.NotFound, status)
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            it.get("/$TEST_PROJECT/version/${TEST_VERSION.versionCode}").apply {
                assertEquals(TEST_VERSION, body())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testVersionList() {
        withTestServer {
            it.get("/$TEST_PROJECT/version").apply {
                assertEquals(emptyList<VersionIndex>(), body())
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            it.get("/$TEST_PROJECT/version").apply {
                assertEquals(TEST_VERSION_INDEX_LIST, body())
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }

    @Test
    fun testLatestDownload() {
        withTestServer {
            it.get("/$TEST_PROJECT/$TEST_CHANNEL").apply {
                assertEquals(HttpStatusCode.NotFound, status)
            }
            assertEquals(VersionManager.addVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION), OperationStatus.SUCCESS)
            it.get("/$TEST_PROJECT/$TEST_CHANNEL").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
            assertEquals(OperationStatus.SUCCESS, VersionManager.deleteVersion(TEST_PROJECT, TEST_CHANNEL, TEST_VERSION.versionCode))
        }
    }
}