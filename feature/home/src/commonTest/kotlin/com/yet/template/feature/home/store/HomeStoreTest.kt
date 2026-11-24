package com.yet.template.feature.home.store

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.yet.template.domain.note.Note
import com.yet.template.domain.note.NoteId
import com.yet.template.domain.note.NoteRepository
import com.yet.template.domain.note.NoteText
import com.yet.template.feature.home.mapper.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeStoreTest {

    private val repository = FakeNoteRepository()
    private lateinit var store: HomeStore

    @BeforeTest
    fun setUp() {
        isAssertOnMainThreadEnabled = false
        Dispatchers.setMain(UnconfinedTestDispatcher())
        store = HomeStoreFactory(DefaultStoreFactory(), repository).create()
    }

    @AfterTest
    fun tearDown() {
        store.dispose()
        Dispatchers.resetMain()
        isAssertOnMainThreadEnabled = true
    }

    @Test
    fun addingANoteClearsTheInputAndShowsTheNote() {
        store.accept(HomeStore.Intent.ChangeText("Buy milk"))
        assertTrue(store.state.toModel().canAdd)

        store.accept(HomeStore.Intent.Add)

        assertEquals("", store.state.text)
        assertEquals(listOf("Buy milk"), store.state.notes.map { it.text })
    }

    @Test
    fun blankTextCannotBeAdded() {
        store.accept(HomeStore.Intent.ChangeText("   "))

        assertFalse(store.state.toModel().canAdd)
    }

    @Test
    fun deleteRemovesTheNote() {
        store.accept(HomeStore.Intent.ChangeText("a"))
        store.accept(HomeStore.Intent.Add)

        store.accept(HomeStore.Intent.Delete(store.state.notes.single().id))

        assertEquals(emptyList(), store.state.notes)
    }
}

private class FakeNoteRepository : NoteRepository {
    override val notes = MutableStateFlow(emptyList<Note>())

    override suspend fun add(text: String) {
        val normalized = NoteText.normalize(text) ?: return
        notes.value = listOf(Note(NoteId(notes.value.size + 1L), normalized, Instant.fromEpochMilliseconds(0))) + notes.value
    }

    override suspend fun delete(id: NoteId) {
        notes.value = notes.value.filterNot { it.id == id }
    }
}
