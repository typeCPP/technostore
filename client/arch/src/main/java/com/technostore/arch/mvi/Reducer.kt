package com.technostore.arch.mvi

interface Reducer<S : State, E : Event> {
    fun reduce(currentState: S, event: E): S
}
