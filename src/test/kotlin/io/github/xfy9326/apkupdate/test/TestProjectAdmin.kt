package io.github.xfy9326.apkupdate.test

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.utils.TEST_PROJECT
import io.github.xfy9326.apkupdate.utils.hasProject
import io.github.xfy9326.apkupdate.utils.withTestServer
import io.ktor.client.request.*
import kotlin.test.Test
import kotlin.test.assertEquals

class TestProjectAdmin {
    @Test
    fun testProjectCreate() {
        withTestServer {
            assert(!hasProject(TEST_PROJECT))
            it.put("/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(hasProject(TEST_PROJECT))
            it.put("/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.EXISTS.statusCode, status.value)
            }
            assertEquals(OperationStatus.SUCCESS, ProjectManager.deleteProject(TEST_PROJECT))
        }
    }

    @Test
    fun testProjectDelete() {
        withTestServer {
            assertEquals(OperationStatus.SUCCESS, ProjectManager.createProject(TEST_PROJECT))
            it.delete("/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.SUCCESS.statusCode, status.value)
            }
            assert(!hasProject(TEST_PROJECT))
            it.delete("/?project=$TEST_PROJECT").apply {
                assertEquals(OperationStatus.NOT_EXISTS.statusCode, status.value)
            }
        }
    }
}