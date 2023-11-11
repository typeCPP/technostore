package com.technostore.arch.mvi

interface EffectHandler<S : State, E : Event> {
    suspend fun process(
        event: E,
        currentState: S,
        store: Store<S, E>,
    )
}
