package com.yet.template.common.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.yet.template.common.AppDispatchers
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object CommonBindings {
    @SingleIn(AppScope::class)
    @Provides
    fun provideStoreFactory(): StoreFactory = DefaultStoreFactory()

    @SingleIn(AppScope::class)
    @Provides
    fun provideAppDispatchers(): AppDispatchers = AppDispatchers()
}
