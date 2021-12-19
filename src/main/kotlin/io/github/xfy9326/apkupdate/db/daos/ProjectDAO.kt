@file:Suppress("unused")

package io.github.xfy9326.apkupdate.db.daos

import io.github.xfy9326.apkupdate.db.tables.VersionTable
import io.github.xfy9326.apkupdate.db.tables.ProjectTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ProjectDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ProjectDAO>(ProjectTable)

    var name by ProjectTable.name
    val apkVersions by VersionDAO referrersOn VersionTable.project
}