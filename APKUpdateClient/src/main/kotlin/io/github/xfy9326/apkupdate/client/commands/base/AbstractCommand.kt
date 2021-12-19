package io.github.xfy9326.apkupdate.client.commands.base

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.output.CliktHelpFormatter

abstract class AbstractCommand(name: String? = null, help: String = "") : CliktCommand(name = name, help = help) {
    init {
        context {
            helpFormatter = CliktHelpFormatter(showDefaultValues = true, showRequiredTag = true)
        }
    }

    final override fun run() {
        try {
            runCommand()
        } catch (e: Throwable) {
            if (e.message != null) {
                println(e.message)
            } else {
                throw e
            }
        }
    }

    protected abstract fun runCommand()
}