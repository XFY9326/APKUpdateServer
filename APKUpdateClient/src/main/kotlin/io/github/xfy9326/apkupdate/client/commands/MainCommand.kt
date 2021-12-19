package io.github.xfy9326.apkupdate.client.commands

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.github.xfy9326.apkupdate.client.commands.base.AbstractCommand
import io.github.xfy9326.apkupdate.client.commands.base.AbstractServerCommand

class MainCommand : AbstractCommand(name = "APKUpdateClient") {
    private val server: String by option(help = "API server url").required()

    override fun runCommand() {
        registeredSubcommands().forEach {
            if (it is AbstractServerCommand) {
                it.server = server
            }
        }
    }
}