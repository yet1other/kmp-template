package com.yet.template.domain.note

import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    /** Every note, newest first; emits again after each change. */
    val notes: Flow<List<Note>>

    /** Adds a note; blank text is ignored (see [NoteText.normalize]). */
    suspend fun add(text: String)

    suspend fun delete(id: NoteId)
}
