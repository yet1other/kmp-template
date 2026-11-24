package com.yet.template.database.note

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.yet.template.common.AppDispatchers
import com.yet.template.database.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NoteDaoTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also {
        AppDatabase.Schema.synchronous().create(it)
    }

    @Test
    fun notesComeBackNewestFirstAndDelete() = runTest {
        val dao = DefaultNoteDao(AppDatabase(driver), AppDispatchers(io = StandardTestDispatcher(testScheduler)))

        dao.insert("first", createdAtMillis = 1)
        dao.insert("second", createdAtMillis = 2)
        val notes = dao.observeAll().first()
        assertEquals(listOf("second", "first"), notes.map { it.text })

        dao.delete(notes.first().id)
        assertEquals(listOf("first"), dao.observeAll().first().map { it.text })
    }
}
