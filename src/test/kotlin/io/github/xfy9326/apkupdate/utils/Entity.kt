package io.github.xfy9326.apkupdate.utils

import io.github.xfy9326.apkupdate.beans.DownloadSource
import io.github.xfy9326.apkupdate.beans.Version
import io.github.xfy9326.apkupdate.beans.toVersionIndex

const val TEST_PROJECT = "TestApp"

const val TEST_CHANNEL = "release"

val TEST_VERSION = Version(
    versionCode = 0,
    versionName = "1.0",
    changeLog = "test\ntest",
    forcedUpdate = true,
    downloadSources = listOf(
        DownloadSource(
            name = "Default1",
            url = "https://localhost:8080",
            directLink = true
        ),
        DownloadSource(
            name = "Default2",
            url = "https://localhost",
            directLink = false
        )
    )
)

val TEST_VERSION_INDEX_LIST = listOf(TEST_VERSION.toVersionIndex(TEST_CHANNEL))