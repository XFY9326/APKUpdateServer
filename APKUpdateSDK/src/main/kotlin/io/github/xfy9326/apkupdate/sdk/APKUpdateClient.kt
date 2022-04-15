package io.github.xfy9326.apkupdate.sdk

import io.github.xfy9326.apkupdate.beans.UpdateContent
import io.github.xfy9326.apkupdate.beans.Version
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json

class APKUpdateClient(private val server: String, private val project: String) {
    private val updateMutex = Mutex()
    private val httpClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(HttpRedirect)
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    suspend fun checkUpdate(
        channel: String,
        version: Int,
        onUpdate: (Version) -> Unit,
        onNoUpdate: () -> Unit,
        onFailed: (Throwable) -> Unit = { it.printStackTrace() }
    ) {
        try {
            if (updateMutex.tryLock(this)) {
                val response = httpClient.get("$server/$project/$channel/latest?version=$version")
                val content = response.body<UpdateContent>()
                val detail = content.detail
                if (content.hasUpdate && detail != null) {
                    onUpdate(detail)
                } else {
                    onNoUpdate()
                }
            }
        } catch (e: Throwable) {
            onFailed(e)
        } finally {
            updateMutex.unlock(this)
        }
    }
}