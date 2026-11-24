package com.yet.template.database.di

import com.yet.template.database.AppDatabase
import com.yet.template.database.driver.SqlDriverFactory
import com.yet.template.database.note.DefaultNoteDao
import com.yet.template.database.note.NoteDao
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
abstract class DatabaseBindings {
    @Binds
    internal abstract val DefaultNoteDao.bindNoteDao: NoteDao

    companion object {
        /** One database per process; the driver opens the file on first query. */
        @SingleIn(AppScope::class)
        @Provides
        fun provideDatabase(driverFactory: SqlDriverFactory): AppDatabase = AppDatabase(driverFactory.create())
    }
}
