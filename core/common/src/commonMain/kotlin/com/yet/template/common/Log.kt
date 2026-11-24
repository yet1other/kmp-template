package com.yet.template.common

import co.touchlab.kermit.Logger

/** The one logging entry point (Kermit: Logcat on Android, OSLog on iOS). Never log secrets. */
object Log {
    fun d(tag: String, message: String, throwable: Throwable? = null) =
        Logger.d(messageString = message, throwable = throwable, tag = tag)

    fun i(tag: String, message: String, throwable: Throwable? = null) =
        Logger.i(messageString = message, throwable = throwable, tag = tag)

    fun w(tag: String, message: String, throwable: Throwable? = null) =
        Logger.w(messageString = message, throwable = throwable, tag = tag)

    fun e(tag: String, message: String, throwable: Throwable? = null) =
        Logger.e(messageString = message, throwable = throwable, tag = tag)
}
