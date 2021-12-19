package io.github.xfy9326.apkupdate.client.commands

import io.github.xfy9326.apkupdate.beans.DownloadSource
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.client.commands.base.AbstractOutputCommand
import io.github.xfy9326.apkupdate.client.utils.GeneralJson
import kotlinx.serialization.encodeToString
import java.io.File

class UpdateTemplateCommand : AbstractOutputCommand(name = "update-template", help = "Generate update template") {
    companion object {
        private const val UPDATE_TEMPLATE_NAME = "update.json"
        private val template = Version(
            versionCode = 1,
            versionName = "1.0",
            changeLog = "",
            forceUpdate = false,
            downloadSources = listOf(
                DownloadSource(
                    name = "",
                    url = "",
                    directLink = true
                )
            )
        )
    }

    override fun runCommand() {
        File(path.toFile(), UPDATE_TEMPLATE_NAME).writeText(GeneralJson.encodeToString(template))
        println("Update template json generated!")
    }
}