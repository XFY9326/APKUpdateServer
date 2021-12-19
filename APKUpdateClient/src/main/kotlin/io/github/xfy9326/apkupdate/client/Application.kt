package io.github.xfy9326.apkupdate.client

import com.github.ajalt.clikt.core.subcommands
import io.github.xfy9326.apkupdate.client.commands.*

fun main(args: Array<String>) =
    MainCommand().subcommands(
        TestCommand(), LatestCommand(), ListVersionCommand(), ShowVersionCommand(), UpdateTemplateCommand(),
        CreateProjectCommand(), DeleteProjectCommand(), AddVersionCommand(), RemoveVersionCommand()
    ).main(args)

