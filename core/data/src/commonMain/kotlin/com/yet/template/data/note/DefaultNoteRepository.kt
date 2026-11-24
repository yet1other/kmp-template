package com.yet.template.data.note

import com.yet.template.database.NoteEntity
import com.yet.template.database.note.NoteDao
import com.yet.template.domain.note.Note
import com.yet.template.domain.note.NoteId
import com.yet.template.domain.note.NoteRepository
import com.yet.template.domain.note.NoteText
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant

@Inject
internal class DefaultNoteRepository(
    private val dao: NoteDao,
    private val clock: Clock,
) : NoteRepository {

    override val notes: Flow<List<Note>> = dao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override suspend fun add(text: String) {
        val normalized = NoteText.normalize(text) ?: return
        dao.insert(normalized, clock.now().toEpochMilliseconds())
    }

    override suspend fun delete(id: NoteId) = dao.delete(id.value)
}

internal fun NoteEntity.toDomain() = Note(
    id = NoteId(id),
    text = text,
    createdAt = Instant.fromEpochMilliseconds(created_at),
)
