package io.github.xfy9326.apkupdate.core

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.db.daos.VersionDAO
import io.github.xfy9326.apkupdate.db.tables.DownloadSourceTable
import io.github.xfy9326.apkupdate.db.tables.VersionTable
import io.github.xfy9326.apkupdate.error.InputUrlException
import io.github.xfy9326.apkupdate.error.UnknownProjectException
import io.ktor.http.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction

object VersionManager {
    fun projectChannelSelector(projectId: EntityID<Int>, channel: String? = null) =
        if (channel == null) {
            (VersionTable.project eq projectId)
        } else {
            (VersionTable.project eq projectId) and (VersionTable.channel eq channel)
        }

    private fun Version.validateDownloadSource() {
        downloadSources.forEach {
            try {
                Url(it.url).toURI()
            } catch (e: Exception) {
                throw InputUrlException(e)
            }
        }
    }

    fun addVersion(projectName: String, channelName: String, version: Version): OperationStatus {
        version.validateDownloadSource()
        return transaction {
            val projectId = ProjectManager.findProjectId(projectName) ?: throw UnknownProjectException()
            val notFoundVersion = VersionDAO.find { VersionTable.versionCode eq version.versionCode }.empty()
            if (notFoundVersion) {
                val versionId = VersionTable.insertAndGetId {
                    it[project] = projectId
                    it[channel] = channelName
                    it[versionCode] = version.versionCode
                    it[versionName] = version.versionName
                    it[forceUpdate] = version.forceUpdate
                    it[changeLog] = version.changeLog
                }
                val result = DownloadSourceTable.batchInsert(version.downloadSources, shouldReturnGeneratedValues = false) {
                    this[DownloadSourceTable.version] = versionId
                    this[DownloadSourceTable.name] = it.name
                    this[DownloadSourceTable.url] = it.url
                    this[DownloadSourceTable.directLink] = it.directLink
                }
                if (result.size != version.downloadSources.size) {
                    OperationStatus.FAILED
                } else {
                    OperationStatus.SUCCESS
                }
            } else {
                OperationStatus.EXISTS
            }
        }
    }

    fun deleteVersion(projectName: String, channelName: String, versionCode: Int): OperationStatus {
        val deleteSize = transaction {
            val projectId = ProjectManager.findProjectId(projectName) ?: throw UnknownProjectException()
            VersionTable.deleteWhere {
                projectChannelSelector(projectId, channelName) and (VersionTable.versionCode eq versionCode)
            }
        }
        return if (deleteSize > 0) {
            OperationStatus.SUCCESS
        } else {
            OperationStatus.NOT_EXISTS
        }
    }
}