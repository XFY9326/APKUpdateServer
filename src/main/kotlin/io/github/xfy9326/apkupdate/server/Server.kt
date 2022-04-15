package io.github.xfy9326.apkupdate.server

import io.github.xfy9326.apkupdate.config.IServerConfig
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.server.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun launchServer(config: IServerConfig) {
    embeddedServer(CIO, host = config.host, port = config.port) {
        checkConfig(config)
        module(config)
    }.start(wait = true)
}

fun Application.module(config: IServerConfig) {
    configureStatusPage()
    configureSerialization(config)
    configureDatabase(config)
    configureAuth(config)
    configureRouting()
}

private fun Application.checkConfig(config: IServerConfig) {
    if (config.token == ServerConfig.DEFAULT_TOKEN) {
        log.warn("Server token should be changed before using!")
    }
}