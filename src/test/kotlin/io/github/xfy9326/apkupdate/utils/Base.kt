package io.github.xfy9326.apkupdate.utils

import io.github.xfy9326.apkupdate.beans.AuthDetails
import io.github.xfy9326.apkupdate.config.GlobalConfig
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.config.configureServerConfig
import io.github.xfy9326.apkupdate.config.hasServerConfig
import io.github.xfy9326.apkupdate.server.module
import io.ktor.application.*
import io.ktor.server.testing.*

fun <R> withTestServer(test: TestApplicationEngine.() -> R) {
    if (!hasServerConfig()) {
        configureServerConfig(ServerConfig(prettyJson = true, dbPath = "db-test/app.db"))
    }
    withTestApplication(Application::module, test)
}

fun getAuthProvider(): TestDigestAuthProvider =
    TestDigestAuthProvider(
        AuthDetails.ADMIN_USER_NAME,
        GlobalConfig.token,
        AuthDetails.AUTH_REALM,
        AuthDetails.AUTH_DIGEST_ALGORITHM
    )
