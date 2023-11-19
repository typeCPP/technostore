package com.technostore.base.presentation

import com.technostore.arch.mvi.InitialState
import com.technostore.arch.mvi.Reducer

class BaseReducer : Reducer<InitialState, BaseEvent> {
    override fun reduce(currentState: InitialState, event: BaseEvent): InitialState {
        return currentState
    }
}