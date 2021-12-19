@file:Suppress("unused")

package io.github.xfy9326.apkupdate.error

class UnknownProjectException : IllegalArgumentException {
    constructor() : super()
    constructor(s: String?) : super(s)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}