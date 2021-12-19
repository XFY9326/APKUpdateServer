package io.github.xfy9326.apkupdate.core

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.db.daos.ProjectDAO
import io.github.xfy9326.apkupdate.db.tables.ProjectTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.transactions.transaction

object ProjectManager {

    fun findProjectId(projectName: String): EntityID<Int>? =
        transaction {
            ProjectDAO.find { ProjectTable.name eq projectName }.limit(1).firstOrNull()?.id
        }

    fun createProject(projectName: String): OperationStatus =
        transaction {
            val notFoundProject = ProjectDAO.find { ProjectTable.name eq projectName }.empty()
            if (notFoundProject) {
                ProjectTable.insertIgnore {
                    it[name] = projectName
                }
                OperationStatus.SUCCESS
            } else {
                OperationStatus.EXISTS
            }
        }

    fun deleteProject(projectName: String): OperationStatus {
        val deleteSize = transaction {
            ProjectTable.deleteWhere {
                ProjectTable.name eq projectName
            }
        }
        return if (deleteSize > 0) {
            OperationStatus.SUCCESS
        } else {
            OperationStatus.NOT_EXISTS
        }
    }
}