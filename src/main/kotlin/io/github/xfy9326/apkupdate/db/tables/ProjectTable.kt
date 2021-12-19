@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package io.github.xfy9326.apkupdate.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object ProjectTable : IntIdTable("projects") {
    val name = text("name").uniqueIndex()
}