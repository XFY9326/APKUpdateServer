import io.github.xfy9326.apkupdate.sdk.APKUpdateClient
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class Test {
    @Test
    fun test() {
        runBlocking {
            APKUpdateClient("http://127.0.0.1:8080", "TestProject")
                .checkUpdate("release", 1,
                    onUpdate = {
                        println(it)
                    }, onFailed = {
                        throw it
                    }, onNoUpdate = {
                        println("No update available")
                    }
                )
        }
    }
}