package io.github.xfy9326.apkupdate.beans

import kotlinx.serialization.Serializable

@Serializable
data class DownloadSource(
    val name: String,
    val url: String,
    val directLink: Boolean
)