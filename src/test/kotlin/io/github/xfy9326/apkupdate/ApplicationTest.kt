@file:Suppress("unused")

package io.github.xfy9326.apkupdate

import io.github.xfy9326.apkupdate.server.launchServer
import io.github.xfy9326.apkupdate.utils.TEST_CONFIG

object ApplicationTest {
    @JvmStatic
    fun main(args: Array<String>) {
        launchServer(TEST_CONFIG)
    }
}