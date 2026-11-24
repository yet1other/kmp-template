package com.yet.template.database.driver

import app.cash.sqldelight.db.SqlDriver

/**
 * Opens the platform driver. The platform graphs in `:sharedLogic` provide it
 * ([androidSqlDriverFactory], [nativeSqlDriverFactory]); tests pass an in-memory one.
 */
fun interface SqlDriverFactory {
    fun create(): SqlDriver
}

internal const val DATABASE_NAME = "app.db"
