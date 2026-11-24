package com.yet.template.feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.yet.template.domain.note.Note
import com.yet.template.domain.note.NoteId

/** The sample screen: a list of notes with an input to add one. */
interface HomeComponent {
    val model: Value<Model>

    fun onTextChanged(text: String)

    fun onAddClicked()

    fun onDeleteClicked(id: NoteId)

    data class Model(
        val notes: List<Note>,
        val text: String,
        val canAdd: Boolean,
    )

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): HomeComponent
    }
}
