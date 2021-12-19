@file:Suppress("unused")

package io.github.xfy9326.apkupdate.db

import io.github.xfy9326.apkupdate.db.tables.DownloadSourceTable
import io.github.xfy9326.apkupdate.db.tables.ProjectTable
import io.github.xfy9326.apkupdate.db.tables.VersionTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object ApplicationDB {
    private const val DB_NAME = "app.db"
    private const val DB_PATH = "db"

    val sqliteDB = Database.connect("jdbc:sqlite:$DB_PATH/$DB_NAME?foreign_keys=on", "org.sqlite.JDBC")

    init {
        require(File(DB_PATH, DB_NAME).let {
            it.exists() || it.parentFile?.isDirectory != false || it.parentFile?.mkdirs() != false
        })
        transaction {
            SchemaUtils.create(ProjectTable, VersionTable, DownloadSourceTable)
        }
    }
}