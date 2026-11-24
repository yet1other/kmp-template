package com.yet.template.database.driver

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.yet.template.database.AppDatabase

fun androidSqlDriverFactory(context: Context): SqlDriverFactory = SqlDriverFactory {
    AndroidSqliteDriver(AppDatabase.Schema.synchronous(), context, DATABASE_NAME)
}
