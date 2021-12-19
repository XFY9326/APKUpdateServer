package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.error.AuthException
import io.github.xfy9326.apkupdate.error.InputUrlException
import io.github.xfy9326.apkupdate.error.UnknownProjectException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import kotlinx.serialization.SerializationException
import org.sqlite.SQLiteException

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
            throw it
        }
        exception<SQLiteException> {
            call.respond(HttpStatusCode.InternalServerError)
            throw it
        }
        exception<InputUrlException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<SerializationException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<UnknownProjectException> {
            call.respond(HttpStatusCode.NotFound)
        }
        exception<AuthException> {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}