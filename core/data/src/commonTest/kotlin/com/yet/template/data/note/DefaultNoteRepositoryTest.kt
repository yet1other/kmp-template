package com.yet.template.data.note

import com.yet.template.database.NoteEntity
import com.yet.template.database.note.NoteDao
import com.yet.template.domain.note.NoteId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultNoteRepositoryTest {

    private val dao = FakeNoteDao()
    private val clock = object : Clock {
        override fun now() = Instant.fromEpochMilliseconds(42)
    }
    private val repository = DefaultNoteRepository(dao, clock)

    @Test
    fun addStoresTrimmedTextWithTheCurrentTime() = runTest {
        repository.add("  hello ")

        val note = repository.notes.first().single()
        assertEquals("hello", note.text)
        assertEquals(Instant.fromEpochMilliseconds(42), note.createdAt)
    }

    @Test
    fun blankTextIsIgnored() = runTest {
        repository.add("   ")

        assertEquals(emptyList(), repository.notes.first())
    }

    @Test
    fun deleteRemovesTheNote() = runTest {
        repository.add("a")
        repository.delete(NoteId(1))

        assertEquals(emptyList(), repository.notes.first())
    }
}

private class FakeNoteDao : NoteDao {
    private val rows = MutableStateFlow(emptyList<NoteEntity>())

    override fun observeAll(): Flow<List<NoteEntity>> = rows

    override suspend fun insert(text: String, createdAtMillis: Long) {
        rows.value = listOf(NoteEntity(rows.value.size + 1L, text, createdAtMillis)) + rows.value
    }

    override suspend fun delete(id: Long) {
        rows.value = rows.value.filterNot { it.id == id }
    }
}
