package io.github.xfy9326.apkupdate.beans

import kotlinx.serialization.Serializable

@Serializable
data class VersionIndex(
    val versionCode: Int,
    val channel: String
)