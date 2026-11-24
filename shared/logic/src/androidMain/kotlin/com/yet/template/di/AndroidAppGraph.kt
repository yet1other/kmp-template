package com.yet.template.di

import android.content.Context
import com.yet.template.database.driver.SqlDriverFactory
import com.yet.template.database.driver.androidSqlDriverFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.createGraphFactory

@DependencyGraph(AppScope::class)
internal interface AndroidAppGraph : AppGraph {

    @Provides
    fun sqlDriverFactory(context: Context): SqlDriverFactory = androidSqlDriverFactory(context)

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides context: Context): AndroidAppGraph
    }
}

fun createAndroidAppGraph(context: Context): AppGraph =
    createGraphFactory<AndroidAppGraph.Factory>().create(context.applicationContext)
