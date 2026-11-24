package com.yet.template.feature.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.value.Value
import com.yet.template.feature.home.HomeComponent
import dev.zacsweers.metro.Inject
import kotlinx.serialization.Serializable

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    private val homeFactory: HomeComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child,
    )

    override fun onBack() = navigation.pop()

    private fun child(config: Config, context: ComponentContext): RootComponent.Child =
        when (config) {
            Config.Home -> RootComponent.Child.Home(homeFactory(context))
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Home : Config
    }
}

@Inject
internal class DefaultRootComponentFactory(
    private val homeFactory: HomeComponent.Factory,
) : RootComponent.Factory {
    override fun invoke(componentContext: ComponentContext): RootComponent =
        DefaultRootComponent(componentContext, homeFactory)
}
