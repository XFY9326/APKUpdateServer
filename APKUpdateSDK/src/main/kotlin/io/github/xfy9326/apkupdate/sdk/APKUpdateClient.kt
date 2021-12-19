package io.github.xfy9326.apkupdate.sdk

import io.github.xfy9326.apkupdate.beans.UpdateContent
import io.github.xfy9326.apkupdate.beans.Version
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.coroutines.sync.Mutex

class APKUpdateClient(private val server: String, private val project: String) {
    private val updateMutex = Mutex()
    private val httpClient: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(HttpRedirect)
            install(JsonFeature) {
                serializer = KotlinxSerializer(kotlinx.serialization.json.Json { ignoreUnknownKeys = true })
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
                val content = httpClient.get<UpdateContent>("$server/$project/$channel/latest?version=$version")
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