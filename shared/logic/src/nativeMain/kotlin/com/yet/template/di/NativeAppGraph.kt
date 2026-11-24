package com.yet.template.di

import com.yet.template.database.driver.SqlDriverFactory
import com.yet.template.database.driver.nativeSqlDriverFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraph

/** The graph of the Apple apps (iOS now, macOS when its target is added). */
@DependencyGraph(AppScope::class)
internal interface NativeAppGraph : AppGraph {

    @Provides
    fun sqlDriverFactory(): SqlDriverFactory = nativeSqlDriverFactory()
}

fun createNativeAppGraph(): AppGraph = createGraph<NativeAppGraph>()
