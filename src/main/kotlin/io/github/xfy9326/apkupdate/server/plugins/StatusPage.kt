package io.github.xfy9326.apkupdate.server.plugins

import io.github.xfy9326.apkupdate.error.AuthException
import io.github.xfy9326.apkupdate.error.InputUrlException
import io.github.xfy9326.apkupdate.error.UnknownProjectException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import org.sqlite.SQLiteException

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError)
            throw cause
        }
        exception<SQLiteException> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError)
            throw cause
        }
        exception<InputUrlException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<SerializationException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<UnknownProjectException> { call, _ ->
            call.respond(HttpStatusCode.NotFound)
        }
        exception<AuthException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}