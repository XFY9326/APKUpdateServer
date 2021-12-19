@file:Suppress("unused")

package io.github.xfy9326.apkupdate.db.daos

import io.github.xfy9326.apkupdate.db.tables.VersionTable
import io.github.xfy9326.apkupdate.db.tables.DownloadSourceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VersionDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VersionDAO>(VersionTable)

    var project by VersionTable.project
    var channel by VersionTable.channel
    var versionCode by VersionTable.versionCode
    var versionName by VersionTable.versionName
    var forceUpdate by VersionTable.forceUpdate
    var changeLog by VersionTable.changeLog
    val downloadSources by DownloadSourceDAO referrersOn DownloadSourceTable.version
}