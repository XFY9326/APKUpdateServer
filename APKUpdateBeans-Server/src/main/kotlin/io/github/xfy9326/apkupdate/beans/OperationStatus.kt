package io.github.xfy9326.apkupdate.beans

enum class OperationStatus(val statusCode: Int, val description: String) {
    SUCCESS(250, "Operation success!"),
    EXISTS(251, "Operation success but data already exists!"),
    NOT_EXISTS(252, "Operation success but data not exists!"),
    FAILED(450, "Operation failed!");

    companion object {
        fun Int.tryToOperationStatus(): OperationStatus? = values().find { it.statusCode == this }
    }
}