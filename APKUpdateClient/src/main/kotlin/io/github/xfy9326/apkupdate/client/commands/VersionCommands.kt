package io.github.xfy9326.apkupdate.client.commands

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import io.github.xfy9326.apkupdate.client.commands.base.AbstractServerCommand
import io.github.xfy9326.apkupdate.client.utils.APIClient
import io.github.xfy9326.apkupdate.client.utils.print
import kotlinx.coroutines.runBlocking

class ListVersionCommand : AbstractServerCommand(name = "version-list", help = "List all version") {
    private val project: String by option(help = "Project name").required()
    private val size by option(help = "List amount").int().restrictTo(min = 1)

    override fun runCommand() {
        runBlocking {
            APIClient(requiredServer()).use { it.getVersions(project, size) }.print()
        }
    }
}

class ShowVersionCommand : AbstractServerCommand(name = "version-show", help = "Show version") {
    private val project: String by option(help = "Project name").required()
    private val version by option(help = "Version code").int().required()

    override fun runCommand() {
        runBlocking {
            APIClient(requiredServer()).use { it.getVersion(project, version) }.print()
        }
    }
}