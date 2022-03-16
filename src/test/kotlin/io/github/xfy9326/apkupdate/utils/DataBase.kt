package io.github.xfy9326.apkupdate.utils

import io.github.xfy9326.apkupdate.core.ProjectManager
import io.github.xfy9326.apkupdate.core.UpdateManager

fun hasProject(name: String): Boolean =
    ProjectManager.findProjectId(name) != null

fun hasVersion(project: String, version: Int): Boolean =
    UpdateManager.getVersion(project, version) != null

fun hasDownloadSource(project: String, channel: String): Boolean =
    UpdateManager.getLatestDownloadUrl(project, channel) != null