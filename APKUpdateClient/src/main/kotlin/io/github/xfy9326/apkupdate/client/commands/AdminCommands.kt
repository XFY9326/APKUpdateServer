package io.github.xfy9326.apkupdate.client.commands

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.client.commands.base.AbstractAdminCommand
import io.github.xfy9326.apkupdate.client.utils.APIClient
import io.github.xfy9326.apkupdate.client.utils.GeneralJson
import io.github.xfy9326.apkupdate.client.utils.print
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import java.io.File

class CreateProjectCommand : AbstractAdminCommand(name = "project-create", help = "Create new project") {
    private val project: String by option(help = "Project name").required()

    override fun runCommand() {
        runBlocking {
            APIClient(requiredServer(), token).use {
                it.createProject(project)
            }.print()
        }
    }
}

class DeleteProjectCommand : AbstractAdminCommand(name = "project-delete", help = "Delete project") {
    private val project: String by option(help = "Project name").required()

    override fun runCommand() {
        runBlocking {
            APIClient(requiredServer(), token).use {
                it.deleteProject(project)
            }.print()
        }
    }
}

class AddVersionCommand : AbstractAdminCommand(name = "version-add", help = "Add new version") {
    private val project: String by option(help = "Project name").required()
    private val channel: String by option(help = "Channel name").required()
    private val json: File by option(help = "Update json").file(mustExist = true, canBeDir = false, mustBeReadable = true).required()

    override fun runCommand() {
        val newVersion = GeneralJson.decodeFromString<Version>(json.readText())
        runBlocking {
            APIClient(requiredServer(), token).use {
                it.addVersion(project, channel, newVersion)
            }.print()
        }
    }
}

class RemoveVersionCommand : AbstractAdminCommand(name = "version-remove", help = "Delete version") {
    private val project: String by option(help = "Project name").required()
    private val channel: String by option(help = "Channel name").required()
    private val version: Int by option(help = "Version code").int().required()

    override fun runCommand() {
        runBlocking {
            APIClient(requiredServer(), token).use {
                it.removeVersion(project, channel, version)
            }.print()
        }
    }
}