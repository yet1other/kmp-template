package com.yet.template.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.yet.template.feature.home.HomeComponent

/**
 * The app's root: a stack of full-screen destinations. [backHandler] drives predictive back on Android
 * and the swipe back on iOS.
 */
interface RootComponent : BackHandlerOwner {
    val stack: Value<ChildStack<*, Child>>

    /** Pops the top destination (the platform back gesture is handled by the stack itself). */
    fun onBack()

    sealed class Child {
        class Home(val component: HomeComponent) : Child()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}
