package com.yet.template.data.di

import com.yet.template.data.note.DefaultNoteRepository
import com.yet.template.domain.note.NoteRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.time.Clock

@ContributesTo(AppScope::class)
@BindingContainer
abstract class DataBindings {
    @Binds
    internal abstract val DefaultNoteRepository.bindNoteRepository: NoteRepository

    companion object {
        @Provides
        fun provideClock(): Clock = Clock.System
    }
}
