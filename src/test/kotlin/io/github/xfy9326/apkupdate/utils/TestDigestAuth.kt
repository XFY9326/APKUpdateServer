package io.github.xfy9326.apkupdate.utils

import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.testing.*
import io.ktor.util.*
import java.security.MessageDigest
import kotlin.collections.set

fun TestApplicationEngine.handleAuthRequest(
    provider: TestDigestAuthProvider,
    method: HttpMethod,
    uri: String,
    setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall {
    handleRequest(method, uri, setup).apply {
        return if (provider.parseResponseHeaders(response)) {
            handleRequest(method, uri) {
                setup()
                provider.addRequestHeaders(this)
            }
        } else {
            this
        }
    }
}

class TestDigestAuthProvider(
    private val username: String,
    private val password: String,
    private val realm: String? = null,
    private val algorithmName: String = "MD5"
) {
    private var serverNonce: String? = null

    private var qop: String? = null
    private var opaque: String? = null
    private val clientNonce = generateNonce()

    private var requestCounter: Int = 0

    fun parseResponseHeaders(response: TestApplicationResponse): Boolean {
        val headerValue = response.headers[HttpHeaders.WWWAuthenticate]
        if (headerValue.isNullOrEmpty()) return false
        val authHeader = parseAuthorizationHeader(headerValue) ?: return false
        if (authHeader !is HttpAuthHeader.Parameterized || authHeader.authScheme != AuthScheme.Digest) return false

        val newNonce = authHeader.parameter("nonce") ?: return false
        val newQop = authHeader.parameter("qop")
        val newOpaque = authHeader.parameter("opaque")

        val newRealm = authHeader.parameter("realm") ?: return false
        if (newRealm != realm && realm != null) {
            return false
        }

        serverNonce = newNonce
        qop = newQop
        opaque = newOpaque

        return true
    }

    fun addRequestHeaders(request: TestApplicationRequest) {
        val nonceCount = ++requestCounter
        val methodName = request.method.value.uppercase()

        val nonce = serverNonce!!
        val serverOpaque = opaque
        val actualQop = qop

        val start = hex("${username}:$realm:${password}".makeDigest())
        val end = hex("$methodName:${request.uri}".makeDigest())
        val tokenSequence = if (actualQop == null) {
            listOf(start, nonce, end)
        } else {
            listOf(start, nonce, nonceCount, clientNonce, actualQop, end)
        }

        val token = tokenSequence.joinToString(":").makeDigest()

        val auth = HttpAuthHeader.Parameterized(
            AuthScheme.Digest,
            linkedMapOf<String, String>().apply {
                realm?.let { this["realm"] = it }
                serverOpaque?.let { this["opaque"] = it }
                this["username"] = username
                this["nonce"] = nonce
                @Suppress("SpellCheckingInspection")
                this["cnonce"] = clientNonce
                this["response"] = hex(token)
                this["uri"] = request.uri
                actualQop?.let { this["qop"] = it }
                this["nc"] = nonceCount.toString()
            }
        )

        request.addHeader(HttpHeaders.Authorization, auth.render())
    }

    private fun String.makeDigest(): ByteArray =
        MessageDigest.getInstance(algorithmName).digest(toByteArray())
}