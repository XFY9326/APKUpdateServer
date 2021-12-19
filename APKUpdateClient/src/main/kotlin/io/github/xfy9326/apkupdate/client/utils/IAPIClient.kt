package io.github.xfy9326.apkupdate.client.utils

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.UpdateContent
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.beans.VersionIndex

interface IAPIClient : AutoCloseable {
    suspend fun testAPIServer(): Boolean

    suspend fun getLatest(project: String, channel: String): Version

    suspend fun getLatest(project: String, channel: String, version: Int): UpdateContent

    suspend fun getVersion(project: String, version: Int): Version

    suspend fun getVersions(project: String, size: Int?): List<VersionIndex>

    suspend fun createProject(project: String): OperationStatus

    suspend fun deleteProject(project: String): OperationStatus

    suspend fun addVersion(project: String, channel: String, version: Version): OperationStatus

    suspend fun removeVersion(project: String, channel: String, version: Int): OperationStatus
}