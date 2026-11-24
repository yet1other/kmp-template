package com.yet.template.feature.home.mapper

import com.yet.template.domain.note.NoteText
import com.yet.template.feature.home.HomeComponent
import com.yet.template.feature.home.store.HomeStore

internal fun HomeStore.State.toModel() = HomeComponent.Model(
    notes = notes,
    text = text,
    canAdd = NoteText.normalize(text) != null,
)
