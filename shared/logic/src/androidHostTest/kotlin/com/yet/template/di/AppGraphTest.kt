package com.yet.template.di

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.yet.template.database.AppDatabase
import com.yet.template.database.driver.SqlDriverFactory
import com.yet.template.domain.note.NoteRepository
import com.yet.template.feature.root.RootComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/** The real graph with every module's contributions, over an in-memory database instead of the Android one. */
@DependencyGraph(AppScope::class)
internal interface TestAppGraph : AppGraph {
    val noteRepository: NoteRepository

    @Provides
    fun sqlDriverFactory(): SqlDriverFactory = SqlDriverFactory {
        JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).also { AppDatabase.Schema.synchronous().create(it) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AppGraphTest {

    private val graph = createGraph<TestAppGraph>()

    @Test
    fun rootStartsOnHome() {
        isAssertOnMainThreadEnabled = false
        Dispatchers.setMain(StandardTestDispatcher())
        try {
            val root = graph.rootComponentFactory(DefaultComponentContext(LifecycleRegistry()))
            assertIs<RootComponent.Child.Home>(root.stack.value.active.instance)
        } finally {
            Dispatchers.resetMain()
            isAssertOnMainThreadEnabled = true
        }
    }

    @Test
    fun notesGoThroughTheWholeDataLayer() = runTest {
        graph.noteRepository.add("  from the graph ")

        assertEquals(listOf("from the graph"), graph.noteRepository.notes.first().map { it.text })
    }
}
