package io.github.xfy9326.apkupdate.client.utils

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.beans.VersionIndex
import kotlinx.serialization.json.Json

val GeneralJson = Json {
    encodeDefaults = true
    prettyPrint = true
    ignoreUnknownKeys = true
}

fun OperationStatus.print() {
    println(description)
}

fun Version.print() =
    println(
        buildString {
            appendLine("VersionCode: $versionCode")
            appendLine("VersionName: $versionName")
            appendLine("ForceUpdate: $forcedUpdate")
            appendLine("ChangeLog: ")
            appendLine("\t" + changeLog.replace("\n", "\n\t"))
            appendLine("Download Sources: ")
            downloadSources.forEach {
                appendLine("\tName: ${it.name}")
                appendLine("\tUrl: ${it.url}")
                appendLine("\tDirectLink: ${it.directLink}")
                appendLine()
            }
        }
    )

fun List<VersionIndex>.print() {
    println(
        buildString {
            appendLine("Versions: ")
            this@print.forEach {
                appendLine("\t${it.versionCode} [${it.channel}]")
            }
        }
    )
}