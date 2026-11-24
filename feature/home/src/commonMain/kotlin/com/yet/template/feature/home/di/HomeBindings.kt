package com.yet.template.feature.home.di

import com.yet.template.feature.home.DefaultHomeComponentFactory
import com.yet.template.feature.home.HomeComponent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
@BindingContainer
abstract class HomeBindings {
    @Binds
    internal abstract val DefaultHomeComponentFactory.bindHomeComponentFactory: HomeComponent.Factory
}
