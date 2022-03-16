@file:Suppress("unused")

package io.github.xfy9326.apkupdate

import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.config.configureServerConfig
import io.github.xfy9326.apkupdate.server.launchServer

object ApplicationTest {
    @JvmStatic
    fun main(args: Array<String>) {
        configureServerConfig(ServerConfig(prettyJson = true))
        launchServer()
    }
}