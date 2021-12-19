package io.github.xfy9326.apkupdate.client.commands.base

import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.nio.file.Path

abstract class AbstractOutputCommand(name: String, help: String) : AbstractCommand(name, help) {
    protected val path: Path by option(help = "Default output path").path(mustExist = true, canBeDir = true, canBeFile = false).defaultLazy {
        Path.of(".")
    }
}