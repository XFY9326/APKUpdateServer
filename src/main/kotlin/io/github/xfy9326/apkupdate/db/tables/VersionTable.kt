@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.xfy9326.apkupdate.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object VersionTable : IntIdTable("versions") {
    val project = reference("project_id", ProjectTable, ReferenceOption.CASCADE, ReferenceOption.CASCADE)
    val channel = text("channel")
    val versionCode = integer("version_code")
    val versionName = text("version_name")
    val forceUpdate = bool("force_update").default(false)
    val changeLog = text("change_log")

    init {
        uniqueIndex(project, versionCode)
    }
}