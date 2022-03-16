@file:Suppress("unused")

package io.github.xfy9326.apkupdate.db

import io.github.xfy9326.apkupdate.config.GlobalConfig
import io.github.xfy9326.apkupdate.db.tables.DownloadSourceTable
import io.github.xfy9326.apkupdate.db.tables.ProjectTable
import io.github.xfy9326.apkupdate.db.tables.VersionTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File

object ApplicationDB {
    private var internalSqliteDB: Database? = null
    val sqliteDB: Database
        get() = internalSqliteDB ?: error("Database hasn't been initialized yet!")

    fun initDB() {
        require(File(GlobalConfig.dbPath).let {
            it.exists() || it.parentFile?.isDirectory != false || it.parentFile?.mkdirs() != false
        })
        internalSqliteDB = Database.connect("jdbc:sqlite:${GlobalConfig.dbPath}?foreign_keys=on", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(ProjectTable, VersionTable, DownloadSourceTable)
        }
    }
}