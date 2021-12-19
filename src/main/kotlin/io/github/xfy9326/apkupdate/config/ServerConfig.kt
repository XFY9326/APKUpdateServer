package io.github.xfy9326.apkupdate.config

import kotlinx.serialization.Serializable

@Serializable
data class ServerConfig(
    override val host: String = DEFAULT_HOST,
    override val port: Int = DEFAULT_PORT,
    override val token: String = DEFAULT_TOKEN,
    override val prettyJson: Boolean = DEFAULT_PRETTY_JSON
) : IServerConfig {
    companion object {
        const val DEFAULT_HOST = "127.0.0.1"
        const val DEFAULT_PORT = 8080
        const val DEFAULT_TOKEN = "password"
        const val DEFAULT_PRETTY_JSON = false
    }
}