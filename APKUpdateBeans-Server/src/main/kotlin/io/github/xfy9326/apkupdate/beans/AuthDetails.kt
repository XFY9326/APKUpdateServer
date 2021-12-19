package io.github.xfy9326.apkupdate.beans

object AuthDetails {
    // Ktor 1.6.7 存在Bug，无法修改为其他摘要算法，详见 https://youtrack.jetbrains.com/issue/KTOR-3391
    const val AUTH_DIGEST_ALGORITHM = "MD5"
    const val AUTH_REALM = "AdministratorOperations"
    const val ADMIN_USER_NAME = "admin"
}