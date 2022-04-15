@file:Suppress("unused")

package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.db.ApplicationDB
import io.ktor.server.application.*

fun Application.configureDatabase() {
    ApplicationDB.initDB()
}