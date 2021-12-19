package io.github.xfy9326.apkupdate.config

interface IServerConfig {
    val host: String
    val port: Int
    val token: String
    val prettyJson: Boolean
}