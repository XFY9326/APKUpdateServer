package io.github.xfy9326.apkupdate.client.commands

import io.github.xfy9326.apkupdate.client.commands.base.AbstractServerCommand
import io.github.xfy9326.apkupdate.client.utils.APIClient
import kotlinx.coroutines.runBlocking

class TestCommand : AbstractServerCommand(name = "test", help = "Test API server connection") {
    override fun runCommand() {
        val hasConnection = runBlocking {
            APIClient(requiredServer()).use { it.testAPIServer() }
        }
        println(if (hasConnection) "API Server is online!" else "API Server connected failed!")
    }
}