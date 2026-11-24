package com.yet.template.database.note

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.yet.template.common.AppDispatchers
import com.yet.template.database.AppDatabase
import com.yet.template.database.NoteEntity
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

interface NoteDao {
    fun observeAll(): Flow<List<NoteEntity>>

    suspend fun insert(text: String, createdAtMillis: Long)

    suspend fun delete(id: Long)
}

@Inject
internal class DefaultNoteDao(
    private val database: AppDatabase,
    private val dispatchers: AppDispatchers,
) : NoteDao {

    override fun observeAll(): Flow<List<NoteEntity>> =
        database.noteQueries.selectAll().asFlow().mapToList(dispatchers.io)

    override suspend fun insert(text: String, createdAtMillis: Long) {
        database.noteQueries.insert(text, createdAtMillis)
    }

    override suspend fun delete(id: Long) {
        database.noteQueries.deleteById(id)
    }
}
