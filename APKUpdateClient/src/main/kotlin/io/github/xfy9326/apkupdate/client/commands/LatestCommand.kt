package io.github.xfy9326.apkupdate.client.commands

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import io.github.xfy9326.apkupdate.client.commands.base.AbstractServerCommand
import io.github.xfy9326.apkupdate.client.utils.APIClient
import io.github.xfy9326.apkupdate.client.utils.print
import kotlinx.coroutines.runBlocking

class LatestCommand : AbstractServerCommand(name = "latest", help = "Get latest version") {
    private val project: String by option(help = "Project name").required()
    private val channel: String by option(help = "Channel name").required()
    private val version: Int? by option(help = "Current version code").int()

    override fun runCommand() {
        val currentVersion = version
        runBlocking {
            val client = APIClient(requiredServer())
            if (currentVersion == null) {
                client.use { it.getLatest(project, channel) }.print()
            } else {
                val content = client.use { it.getLatest(project, channel, currentVersion) }
                val detail = content.detail
                if (content.hasUpdate && detail != null) {
                    detail.print()
                } else {
                    println("Current version is the latest!")
                }
            }
        }
    }
}