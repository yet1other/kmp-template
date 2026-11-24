package com.yet.template.feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.yet.template.common.decompose.asValue
import com.yet.template.domain.note.NoteId
import com.yet.template.feature.home.mapper.toModel
import com.yet.template.feature.home.store.HomeStore
import com.yet.template.feature.home.store.HomeStoreFactory
import dev.zacsweers.metro.Inject

internal class DefaultHomeComponent(
    componentContext: ComponentContext,
    storeFactory: HomeStoreFactory,
) : HomeComponent, ComponentContext by componentContext {

    // Retained across configuration changes (instanceKeeper) and disposed with the component.
    private val store = instanceKeeper.getStore { storeFactory.create() }

    override val model: Value<HomeComponent.Model> = store.asValue().map { it.toModel() }

    override fun onTextChanged(text: String) = store.accept(HomeStore.Intent.ChangeText(text))

    override fun onAddClicked() = store.accept(HomeStore.Intent.Add)

    override fun onDeleteClicked(id: NoteId) = store.accept(HomeStore.Intent.Delete(id))
}

@Inject
internal class DefaultHomeComponentFactory(
    private val storeFactory: HomeStoreFactory,
) : HomeComponent.Factory {
    override fun invoke(componentContext: ComponentContext): HomeComponent =
        DefaultHomeComponent(componentContext, storeFactory)
}
