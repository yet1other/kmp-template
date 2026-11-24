package com.yet.template.database.driver

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.yet.template.database.AppDatabase

/** Apple targets (iOS, later macOS): the system SQLite. */
fun nativeSqlDriverFactory(): SqlDriverFactory = SqlDriverFactory {
    NativeSqliteDriver(AppDatabase.Schema.synchronous(), DATABASE_NAME)
}
