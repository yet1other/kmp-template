package com.yet.template.feature.home.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.yet.template.domain.note.NoteRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.launch

@Inject
internal class HomeStoreFactory(
    private val storeFactory: StoreFactory,
    private val notes: NoteRepository,
) {
    fun create(): HomeStore =
        object : HomeStore, Store<HomeStore.Intent, HomeStore.State, Nothing> by storeFactory.create(
            name = "HomeStore",
            initialState = HomeStore.State(),
            bootstrapper = SimpleBootstrapper(HomeStore.Action.ObserveNotes),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl,
        ) {}

    private inner class ExecutorImpl :
        CoroutineExecutor<HomeStore.Intent, HomeStore.Action, HomeStore.State, HomeStore.Msg, Nothing>() {

        override fun executeAction(action: HomeStore.Action) {
            when (action) {
                HomeStore.Action.ObserveNotes ->
                    scope.launch { notes.notes.collect { dispatch(HomeStore.Msg.NotesLoaded(it)) } }
            }
        }

        override fun executeIntent(intent: HomeStore.Intent) {
            when (intent) {
                is HomeStore.Intent.ChangeText -> dispatch(HomeStore.Msg.TextChanged(intent.text))
                HomeStore.Intent.Add -> {
                    val text = state().text
                    dispatch(HomeStore.Msg.TextChanged(""))
                    scope.launch { notes.add(text) }
                }
                is HomeStore.Intent.Delete -> scope.launch { notes.delete(intent.id) }
            }
        }
    }

    private object ReducerImpl : Reducer<HomeStore.State, HomeStore.Msg> {
        override fun HomeStore.State.reduce(msg: HomeStore.Msg): HomeStore.State =
            when (msg) {
                is HomeStore.Msg.NotesLoaded -> copy(notes = msg.notes)
                is HomeStore.Msg.TextChanged -> copy(text = msg.text)
            }
    }
}
