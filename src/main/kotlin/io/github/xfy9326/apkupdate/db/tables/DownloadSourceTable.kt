@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.xfy9326.apkupdate.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object DownloadSourceTable : IntIdTable("download_sources") {
    val version = reference("version_id", VersionTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val name = text("name")
    val url = text("url")
    val directLink = bool("direct_link").default(false)
}