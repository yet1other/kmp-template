package com.yet.template.feature.home

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.yet.template.domain.note.Note
import com.yet.template.domain.note.NoteId
import kotlin.time.Instant

/** For Compose previews and UI tests. */
class PreviewHomeComponent(
    notes: List<Note> = listOf(
        Note(NoteId(1), "Rename the template with scripts/rename.sh", Instant.fromEpochMilliseconds(0)),
        Note(NoteId(2), "Replace the Note sample with your domain", Instant.fromEpochMilliseconds(0)),
    ),
) : HomeComponent {
    override val model: Value<HomeComponent.Model> =
        MutableValue(HomeComponent.Model(notes = notes, text = "", canAdd = false))

    override fun onTextChanged(text: String) = Unit
    override fun onAddClicked() = Unit
    override fun onDeleteClicked(id: NoteId) = Unit
}
