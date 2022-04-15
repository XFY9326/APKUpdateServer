package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.beans.AuthDetails
import io.github.xfy9326.apkupdate.config.GlobalConfig
import io.github.xfy9326.apkupdate.error.AuthException
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.security.MessageDigest

const val AUTH_DIGEST_ADMIN = "admin-auth"

fun String.getAuthDigest(): ByteArray =
    MessageDigest.getInstance(AuthDetails.AUTH_DIGEST_ALGORITHM).digest(toByteArray())

fun Application.configureAuth() {
    authentication {
        digest(AUTH_DIGEST_ADMIN) {
            realm = AuthDetails.AUTH_REALM
            algorithmName = AuthDetails.AUTH_DIGEST_ALGORITHM
            digestProvider { userName, realm ->
                if (userName == AuthDetails.ADMIN_USER_NAME) {
                    "$userName:$realm:${GlobalConfig.token}".getAuthDigest()
                } else {
                    throw AuthException("User name '$userName' can't be resolved!")
                }
            }
        }
    }
}