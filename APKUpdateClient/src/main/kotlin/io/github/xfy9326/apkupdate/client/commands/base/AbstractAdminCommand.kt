package io.github.xfy9326.apkupdate.client.commands.base

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

abstract class AbstractAdminCommand(name: String? = null, help: String = "") : AbstractServerCommand(name, help) {
    protected val token: String by option(help = "Server token").required()
}