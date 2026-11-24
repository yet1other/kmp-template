package com.yet.template

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.yet.template.di.createNativeAppGraph
import com.yet.template.feature.root.RootComponent
import platform.Foundation.NSCoder

/**
 * The graph, the root component and its saved state for an Apple app. The platform entry point passes the
 * app's [lifecycle] (iOS: `ApplicationLifecycle()`), forwards the system's state restoration to
 * [saveState] / [restoreState] and reads [component] once the UI is created (after any restore).
 */
class AppRoot(private val lifecycle: Lifecycle) {

    private var stateKeeper = StateKeeperDispatcher(savedState = null)

    val component: RootComponent by lazy {
        createNativeAppGraph().rootComponentFactory(
            DefaultComponentContext(lifecycle = lifecycle, stateKeeper = stateKeeper),
        )
    }

    fun saveState(coder: NSCoder) = save(coder, stateKeeper.save())

    fun restoreState(coder: NSCoder) {
        stateKeeper = StateKeeperDispatcher(savedState = restore(coder))
    }
}
