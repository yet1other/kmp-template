package com.yet.template.di

import com.yet.template.feature.root.RootComponent

/**
 * What the platform apps take from the graph. The `@DependencyGraph` itself lives in each platform
 * source set ([AndroidAppGraph], `NativeAppGraph` in nativeMain) so it can provide platform-only bindings.
 */
interface AppGraph {
    val rootComponentFactory: RootComponent.Factory
}
