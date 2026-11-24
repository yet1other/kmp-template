package com.yet.template.feature.root

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.yet.template.feature.home.PreviewHomeComponent
import kotlin.test.Test
import kotlin.test.assertIs

class RootComponentTest {

    @Test
    fun startsOnHome() {
        val root = DefaultRootComponentFactory(homeFactory = { PreviewHomeComponent() })(
            DefaultComponentContext(LifecycleRegistry()),
        )

        assertIs<RootComponent.Child.Home>(root.stack.value.active.instance)
    }
}
