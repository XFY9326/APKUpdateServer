package io.github.xfy9326.apkupdate.beans

import kotlinx.serialization.Serializable

@Serializable
data class UpdateContent(
    val hasUpdate: Boolean,
    val detail: Version? = null
) {
    init {
        require((hasUpdate && detail != null) || (!hasUpdate && detail == null))
    }
}
