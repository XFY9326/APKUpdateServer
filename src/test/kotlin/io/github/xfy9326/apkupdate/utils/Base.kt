package io.github.xfy9326.apkupdate.utils

import io.github.xfy9326.apkupdate.beans.AuthDetails
import io.github.xfy9326.apkupdate.config.ServerConfig
import io.github.xfy9326.apkupdate.db.ApplicationDB
import io.github.xfy9326.apkupdate.server.module
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json

val TEST_CONFIG = ServerConfig(prettyJson = true, dbPath = "db-test/app.db")

fun <R> withTestServer(test: suspend (HttpClient) -> R) {
    testApplication {
        application {
            module(TEST_CONFIG)
        }
        externalServices {
            hosts("https://localhost:8080/") {
                routing {
                    get("/") {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
        }
        val httpClient = createClient {
            ApplicationDB.initDB(TEST_CONFIG)
            install(HttpRedirect)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            Auth {
                digest {
                    algorithmName = AuthDetails.AUTH_DIGEST_ALGORITHM
                    realm = AuthDetails.AUTH_REALM
                    credentials {
                        DigestAuthCredentials(username = AuthDetails.ADMIN_USER_NAME, password = TEST_CONFIG.token)
                    }
                }
            }
        }
        test(httpClient)
    }
}
