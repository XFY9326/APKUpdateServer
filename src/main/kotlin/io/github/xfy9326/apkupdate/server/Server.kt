package io.github.xfy9326.apkupdate.server

import io.github.xfy9326.apkupdate.config.IServerConfig
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.config.configureServerConfig
import io.github.xfy9326.apkupdate.server.plugins.*
import io.ktor.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun launchServer(config: IServerConfig) {
    configureServerConfig(config)
    embeddedServer(CIO, host = config.host, port = config.port) {
        checkGlobalConfig(config)
        module()
    }.start(wait = true)
}

fun Application.module() {
    configureStatusPage()
    configureSerialization()
    configureDatabase()
    configureAuth()
    configureRouting()
}

private fun Application.checkGlobalConfig(config: IServerConfig) {
    if (config.token == ServerConfig.DEFAULT_TOKEN) {
        log.warn("Server token should be changed before using!")
    }
}