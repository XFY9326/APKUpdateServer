package io.github.xfy9326.apkupdate.beans

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val versionCode: Int,
    val versionName: String,
    val changeLog: String,
    val forceUpdate: Boolean,
    val downloadSources: List<DownloadSource>
)