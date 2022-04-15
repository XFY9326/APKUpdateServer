package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.config.GlobalConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

private var generalJson: Json? = null

val GeneralJson: Json
    get() = generalJson ?: error("Serialization module hasn't been initialized yet!")

fun Application.configureSerialization() {
    generalJson = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        prettyPrint = GlobalConfig.prettyJson
    }
    install(ContentNegotiation) {
        json(GeneralJson)
    }
}