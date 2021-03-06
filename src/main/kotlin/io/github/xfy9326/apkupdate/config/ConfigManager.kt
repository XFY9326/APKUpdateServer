package io.github.xfy9326.apkupdate.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.io.path.readText

private const val DEFAULT_SERVER_CONFIG_NAME = "config.default.json"

private val ConfigJson = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
}

fun Path.readServerConfig(): IServerConfig =
    ConfigJson.decodeFromString<ServerConfig>(readText())

fun Path.writeDefaultServerConfigInDir() =
    File(toFile(), DEFAULT_SERVER_CONFIG_NAME).writeText(ConfigJson.encodeToString(ServerConfig()))