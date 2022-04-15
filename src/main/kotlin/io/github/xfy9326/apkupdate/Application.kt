package io.github.xfy9326.apkupdate

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.CliktHelpFormatter
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import io.github.xfy9326.apkupdate.config.IServerConfig
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.config.readServerConfig
import io.github.xfy9326.apkupdate.config.writeDefaultServerConfigInDir
import io.github.xfy9326.apkupdate.server.launchServer
import java.nio.file.Path

fun main(args: Array<String>) =
    APKUpdateServer().subcommands(Launch(), Config()).main(args)

private class APKUpdateServer : NoOpCliktCommand(name = "APKUpdateServer")

private class Launch : CliktCommand(help = "Launch server"), IServerConfig {
    override val host: String by option(help = "Listening host").default(ServerConfig.DEFAULT_HOST)
    override val port: Int by option(help = "Listening port").int().default(ServerConfig.DEFAULT_PORT)
    override val token: String by option(help = "User token").default(ServerConfig.DEFAULT_TOKEN)
    override val prettyJson: Boolean by option(help = "Print pretty Json").flag(default = ServerConfig.DEFAULT_PRETTY_JSON)
    override val dbPath: String by option(help = "SQLite db path").default(ServerConfig.DEFAULT_DB_PATH)
    private val config: Path? by option(help = "Config json").path(mustExist = true, canBeDir = false, mustBeReadable = true)

    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true)
        }
    }

    override fun run() {
        launchServer(config?.readServerConfig() ?: this)
    }
}

private class Config : CliktCommand(help = "Generate default config") {
    private val path: Path by option(help = "Default config output path").path(mustExist = true, canBeDir = true, canBeFile = false).defaultLazy {
        Path.of(".")
    }

    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true)
        }
    }

    override fun run() {
        path.writeDefaultServerConfigInDir()
        println("Default config json generated!")
    }
}