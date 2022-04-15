package io.github.xfy9326.apkupdate.utils

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.util.*
import java.security.MessageDigest
import kotlin.collections.set

suspend fun HttpClient.authRequest(
    provider: TestDigestAuthProvider,
    httpMethod: HttpMethod,
    uri: String,
    setup: (HttpRequestBuilder) -> Unit = {}
): HttpResponse {
    return request(uri) {
        method = httpMethod
        setup(this)
    }.let {
        if (provider.parseResponseHeaders(it)) {
            request(uri) {
                method = httpMethod
                setup(this)
                provider.addRequestHeaders(this)
            }
        } else {
            it
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

    fun parseResponseHeaders(response: HttpResponse): Boolean {
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

    fun addRequestHeaders(request: HttpRequestBuilder) {
        val nonceCount = ++requestCounter
        val methodName = request.method.value.uppercase()
        val url = request.url.buildString()

        val nonce = serverNonce!!
        val serverOpaque = opaque
        val actualQop = qop

        val start = hex("${username}:$realm:${password}".makeDigest())
        val end = hex("$methodName:${url}".makeDigest())
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
                this["uri"] = url
                actualQop?.let { this["qop"] = it }
                this["nc"] = nonceCount.toString()
            }
        )

        request.headers.append(HttpHeaders.Authorization, auth.render())
    }

    private fun String.makeDigest(): ByteArray =
        MessageDigest.getInstance(algorithmName).digest(toByteArray())
}