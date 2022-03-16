package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.utils.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProjectAdmin {
    @Test
    fun testProjectCreate() {
        withTestServer {
            val authProvider = getAuthProvider()
            assert(!hasProject(TEST_PROJECT))
            handleAuthRequest(authProvider, HttpMethod.Put, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, response.status()?.value)
            }
            assert(hasProject(TEST_PROJECT))
            handleAuthRequest(authProvider, HttpMethod.Put, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.EXISTS.statusCode, response.status()?.value)
            }
            assertEquals(OperationStatus.SUCCESS, ProjectManager.deleteProject(TEST_PROJECT))
        }
    }

    @Test
    fun testProjectDelete() {
        withTestServer {
            val authProvider = getAuthProvider()
            assertEquals(OperationStatus.SUCCESS, ProjectManager.createProject(TEST_PROJECT))
            handleAuthRequest(authProvider, HttpMethod.Delete, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, response.status()?.value)
            }
            assert(!hasProject(TEST_PROJECT))
            handleAuthRequest(authProvider, HttpMethod.Delete, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.NOT_EXISTS.statusCode, response.status()?.value)
            }
        }
    }
}