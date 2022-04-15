package io.github.xfy9326.apkupdate.client.utils

import io.github.xfy9326.apkupdate.beans.*
import io.github.xfy9326.apkupdate.beans.OperationStatus.Companion.tryToOperationStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class APIClient(private val server: String, private val token: String? = null) : IAPIClient {
    private val httpClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(HttpRedirect)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            if (token != null) {
                Auth {
                    digest {
                        algorithmName = AuthDetails.AUTH_DIGEST_ALGORITHM
                        realm = AuthDetails.AUTH_REALM
                        credentials {
                            DigestAuthCredentials(username = AuthDetails.ADMIN_USER_NAME, password = token)
                        }
                    }
                }
            }
        }
    }

    override suspend fun testAPIServer(): Boolean {
        return runCatching {
            httpClient.head(server).status == HttpStatusCode.OK
        }.getOrDefault(false)
    }

    private suspend inline fun <reified T> handleSimpleAPIGet(url: String, project: String, channel: String? = null, version: Int? = null): T =
        httpClient.get(url) {
            expectSuccess = false
        }.let {
            when (it.status) {
                HttpStatusCode.OK -> it.body()
                HttpStatusCode.NotFound -> {
                    if (channel != null) {
                        error("Project '$project' or Channel '$channel' not found!")
                    } else if (version != null) {
                        error("Project '$project' or Version '$version' not found!")
                    } else {
                        error("Project '$project' not found!")
                    }
                }
                else -> error("Request failed! Code: ${it.status.value} Reason: ${it.status.description}")
            }
        }

    override suspend fun getLatest(project: String, channel: String): Version =
        handleSimpleAPIGet("$server/$project/$channel/latest", project, channel)

    override suspend fun getLatest(project: String, channel: String, version: Int): UpdateContent =
        handleSimpleAPIGet("$server/$project/$channel/latest?version=$version", project, channel)

    override suspend fun getVersion(project: String, version: Int): Version =
        handleSimpleAPIGet("$server/$project/version/$version", project, version = version)

    override suspend fun getVersions(project: String, size: Int?): List<VersionIndex> =
        handleSimpleAPIGet(
            if (size == null) {
                "$server/$project/version"
            } else {
                "$server/$project/version?size=$size"
            }, project
        )

    private suspend fun handleAuthAPIRequest(url: String, httpMethod: HttpMethod, project: String? = null, content: Any? = null): OperationStatus =
        httpClient.request(url) {
            expectSuccess = false
            method = httpMethod
            if (content != null) {
                contentType(ContentType.Application.Json)
                setBody(content)
            }
        }.let {
            if (it.status == HttpStatusCode.BadRequest) {
                error("Request error! Code: ${it.status.value} Reason: ${it.status.description}")
            } else if (it.status == HttpStatusCode.NotFound) {
                if (project != null) {
                    error("Project '$project' not found!")
                } else {
                    error("API not found!")
                }
            } else {
                it.status.value.tryToOperationStatus() ?: error("Request failed! Code: ${it.status.value} Reason: ${it.status.description}")
            }
        }

    override suspend fun createProject(project: String): OperationStatus =
        handleAuthAPIRequest("$server?project=$project", HttpMethod.Put)

    override suspend fun deleteProject(project: String): OperationStatus =
        handleAuthAPIRequest("$server?project=$project", HttpMethod.Delete)

    override suspend fun addVersion(project: String, channel: String, version: Version): OperationStatus =
        handleAuthAPIRequest("$server/$project/$channel", HttpMethod.Put, project, version)

    override suspend fun removeVersion(project: String, channel: String, version: Int): OperationStatus =
        handleAuthAPIRequest("$server/$project/$channel?version=$version", HttpMethod.Delete, project)

    override fun close() {
        httpClient.close()
    }
}