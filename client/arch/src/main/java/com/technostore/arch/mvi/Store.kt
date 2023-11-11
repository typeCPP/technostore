package com.technostore.arch.mvi

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class Store<S : State, A : Event>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val effectHandlers: List<EffectHandler<S, A>> = emptyList(),
) {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    private val currentState: S
        get() = _state.value

    private var baseViewModel: BaseViewModel? = null

    fun setViewModel(viewModel: BaseViewModel) {
        baseViewModel = viewModel
    }

    suspend fun dispatch(action: A) {
        effectHandlers.forEach { middleware ->
            middleware.process(action, currentState, this)
        }

        val newState = reducer.reduce(currentState, action)
        _state.value = newState
    }

    fun acceptNews(news: News) {
        Log.wtf("Store", "Store")
        baseViewModel?.posNews(news)
    }
}
