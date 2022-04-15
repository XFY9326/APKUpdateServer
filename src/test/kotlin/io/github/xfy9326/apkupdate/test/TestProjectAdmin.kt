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
            it.authRequest(authProvider, HttpMethod.Put, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(hasProject(TEST_PROJECT))
            it.authRequest(authProvider, HttpMethod.Put, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.EXISTS.statusCode, status.value)
            }
            assertEquals(OperationStatus.SUCCESS, ProjectManager.deleteProject(TEST_PROJECT))
        }
    }

    @Test
    fun testProjectDelete() {
        withTestServer {
            val authProvider = getAuthProvider()
            assertEquals(OperationStatus.SUCCESS, ProjectManager.createProject(TEST_PROJECT))
            it.authRequest(authProvider, HttpMethod.Delete, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(!hasProject(TEST_PROJECT))
            it.authRequest(authProvider, HttpMethod.Delete, "/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.NOT_EXISTS.statusCode, status.value)
            }
        }
    }
}