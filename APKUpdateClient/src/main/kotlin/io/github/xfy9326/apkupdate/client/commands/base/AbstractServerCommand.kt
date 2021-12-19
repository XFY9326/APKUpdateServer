package io.github.xfy9326.apkupdate.client.commands.base

abstract class AbstractServerCommand(name: String?, help: String = "") : AbstractCommand(name, help) {
    var server: String? = null

    protected fun requiredServer() =
        server ?: error("Empty server!")
}