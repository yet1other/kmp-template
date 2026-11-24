package com.yet.template.feature.home.store

import com.arkivanov.mvikotlin.core.store.Store
import com.yet.template.domain.note.Note
import com.yet.template.domain.note.NoteId

internal interface HomeStore : Store<HomeStore.Intent, HomeStore.State, Nothing> {

    sealed interface Intent {
        data class ChangeText(val text: String) : Intent
        data object Add : Intent
        data class Delete(val id: NoteId) : Intent
    }

    data class State(
        val notes: List<Note> = emptyList(),
        val text: String = "",
    )

    sealed interface Action {
        data object ObserveNotes : Action
    }

    sealed interface Msg {
        data class NotesLoaded(val notes: List<Note>) : Msg
        data class TextChanged(val text: String) : Msg
    }
}
