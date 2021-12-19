@file:Suppress("unused")

package io.github.xfy9326.apkupdate.db.daos

import io.github.xfy9326.apkupdate.db.tables.DownloadSourceTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DownloadSourceDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DownloadSourceDAO>(DownloadSourceTable)

    var name by DownloadSourceTable.name
    var url by DownloadSourceTable.url
    var directLink by DownloadSourceTable.directLink
}