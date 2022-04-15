package io.github.xfy9326.apkupdate.utils

import io.github.xfy9326.apkupdate.beans.AuthDetails
import io.github.xfy9326.apkupdate.config.GlobalConfig
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.config.configureServerConfig
import io.github.xfy9326.apkupdate.config.hasServerConfig
import io.github.xfy9326.apkupdate.server.module
import io.ktor.client.*
import io.ktor.server.testing.*

fun <R> withTestServer(test: suspend (HttpClient) -> R) {
    if (!hasServerConfig()) {
        configureServerConfig(ServerConfig(prettyJson = true, dbPath = "db-test/app.db"))
    }
    testApplication {
        application { module() }
        test(client)
    }
}

fun getAuthProvider(): TestDigestAuthProvider =
    TestDigestAuthProvider(
        AuthDetails.ADMIN_USER_NAME,
        GlobalConfig.token,
        AuthDetails.AUTH_REALM,
        AuthDetails.AUTH_DIGEST_ALGORITHM
    )
