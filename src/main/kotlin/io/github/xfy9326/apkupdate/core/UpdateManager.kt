package io.github.xfy9326.apkupdate.core

import io.github.xfy9326.apkupdate.beans.DownloadSource
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.beans.VersionIndex
import io.github.xfy9326.apkupdate.db.daos.VersionDAO
import io.github.xfy9326.apkupdate.db.tables.VersionTable
import io.github.xfy9326.apkupdate.error.UnknownProjectException
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object UpdateManager {
    private const val DEFAULT_VERSION_SHOW_SIZE = 10

    private fun VersionDAO.convertToVersion(needsForceUpdate: Boolean? = null) =
        Version(
            versionCode = versionCode,
            versionName = versionName,
            changeLog = changeLog,
            forceUpdate = needsForceUpdate ?: forceUpdate,
            downloadSources = downloadSources.map {
                DownloadSource(
                    name = it.name,
                    url = it.url,
                    directLink = it.directLink
                )
            }
        )

    private fun queryVersion(projectName: String, channel: String? = null, versionCode: Int? = null, versionRange: Boolean = false): Query {
        val projectId = ProjectManager.findProjectId(projectName) ?: throw UnknownProjectException()
        return VersionTable.select {
            var statement = VersionManager.projectChannelSelector(projectId, channel)
            if (versionCode != null) {
                statement = if (versionRange) {
                    statement and (VersionTable.versionCode greater versionCode)
                } else {
                    statement and (VersionTable.versionCode eq versionCode)
                }
            }
            statement
        }.orderBy(VersionTable.versionCode to SortOrder.DESC)
    }

    fun getLatestVersion(projectName: String, channel: String, versionCode: Int?): Version? {
        val versionQuery = queryVersion(projectName, channel, versionCode, true)
        return transaction {
            val latest = versionQuery.limit(1).firstOrNull()?.let { VersionDAO.wrapRow(it) } ?: return@transaction null
            if (versionCode == null) {
                latest.convertToVersion()
            } else {
                val needsForceUpdate = !versionQuery.adjustSlice {
                    slice(VersionTable.forceUpdate)
                }.adjustWhere {
                    this?.and(VersionTable.forceUpdate eq true) ?: (VersionTable.forceUpdate eq true)
                }.empty()
                latest.convertToVersion(needsForceUpdate)
            }
        }
    }

    fun getVersions(projectName: String, size: Int?): List<VersionIndex> {
        val projectId = ProjectManager.findProjectId(projectName) ?: throw UnknownProjectException()
        val query = VersionTable.select {
            VersionManager.projectChannelSelector(projectId)
        }.adjustSlice {
            slice(VersionTable.versionCode, VersionTable.channel)
        }.orderBy(VersionTable.versionCode, SortOrder.DESC).limit(size ?: DEFAULT_VERSION_SHOW_SIZE)
        return transaction {
            query.map {
                VersionIndex(it[VersionTable.versionCode], it[VersionTable.channel])
            }
        }
    }

    fun getVersion(projectName: String, versionCode: Int): Version? =
        transaction {
            queryVersion(projectName, null, versionCode).limit(1).firstOrNull()?.let {
                VersionDAO.wrapRow(it)
            }?.convertToVersion()
        }

    fun getLatestDownloadUrl(projectName: String, channel: String): String? =
        transaction {
            val version = VersionDAO.wrapRows(queryVersion(projectName, channel))
                .firstOrNull { !it.downloadSources.empty() } ?: return@transaction null
            version.downloadSources.find { it.directLink }?.url ?: version.downloadSources.firstOrNull()?.url
        }
}