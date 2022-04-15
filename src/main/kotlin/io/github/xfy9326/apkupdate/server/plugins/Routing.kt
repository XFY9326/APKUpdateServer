package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.beans.OperationStatus
import io.github.xfy9326.apkupdate.beans.UpdateContent
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.UpdateManager
import io.github.xfy9326.apkupdate.core.VersionManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val PROJECT = "project"
private const val CHANNEL = "channel"
private const val VERSION = "version"
private const val SIZE = "size"

fun Application.configureRouting() {
    routing {
        head("/") {
            call.respond(HttpStatusCode.OK)
        }
        get("/") {
            call.respond(HttpStatusCode.OK)
        }
        get("/{$PROJECT}/{$CHANNEL}/latest") {
            val project = call.parameters[PROJECT] ?: return@get call.respond(HttpStatusCode.NotFound)
            val channel = call.parameters[CHANNEL] ?: return@get call.respond(HttpStatusCode.NotFound)
            val version = call.parameters[VERSION]?.toIntOrNull()
            val response = UpdateManager.getLatestVersion(project, channel, version)
            if (version == null) {
                if (response == null) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(response)
                }
            } else {
                if (response == null) {
                    call.respond(UpdateContent(false))
                } else {
                    call.respond(UpdateContent(true, response))
                }
            }
        }
        get("/{$PROJECT}/version") {
            val project = call.parameters[PROJECT] ?: return@get call.respond(HttpStatusCode.NotFound)
            val size = call.parameters[SIZE]?.toIntOrNull()
            val response = UpdateManager.getVersions(project, size)
            call.respond(response)
        }
        get("/{$PROJECT}/version/{$VERSION}") {
            val project = call.parameters[PROJECT] ?: return@get call.respond(HttpStatusCode.NotFound)
            val version = call.parameters[VERSION]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.NotFound)
            val response = UpdateManager.getVersion(project, version)
            if (response == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(response)
            }
        }
        get("/{$PROJECT}/{$CHANNEL}") {
            val project = call.parameters[PROJECT] ?: return@get call.respond(HttpStatusCode.NotFound)
            val channel = call.parameters[CHANNEL] ?: return@get call.respond(HttpStatusCode.NotFound)
            val url = UpdateManager.getLatestDownloadUrl(project, channel)
            if (url == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respondRedirect(url, false)
            }
        }
        authenticate(AUTH_DIGEST_ADMIN) {
            put("/{$PROJECT}/{$CHANNEL}") {
                val project = call.parameters[PROJECT] ?: return@put call.respond(HttpStatusCode.NotFound)
                val channel = call.parameters[CHANNEL] ?: return@put call.respond(HttpStatusCode.NotFound)
                val version = call.receive<Version>()
                val response = VersionManager.addVersion(project, channel, version)
                call.respond(response.toHttpStatusCode())
            }
            delete("/{$PROJECT}/{$CHANNEL}") {
                val project = call.parameters[PROJECT] ?: return@delete call.respond(HttpStatusCode.NotFound)
                val channel = call.parameters[CHANNEL] ?: return@delete call.respond(HttpStatusCode.NotFound)
                val version = call.parameters[VERSION]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val response = VersionManager.deleteVersion(project, channel, version)
                call.respond(response.toHttpStatusCode())
            }
            put("/") {
                val project = call.parameters[PROJECT] ?: return@put call.respond(HttpStatusCode.BadRequest)
                val response = ProjectManager.createProject(project)
                call.respond(response.toHttpStatusCode())
            }
            delete("/") {
                val project = call.parameters[PROJECT] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val response = ProjectManager.deleteProject(project)
                call.respond(response.toHttpStatusCode())
            }
        }
    }
}

private fun OperationStatus.toHttpStatusCode() = HttpStatusCode(statusCode, description)