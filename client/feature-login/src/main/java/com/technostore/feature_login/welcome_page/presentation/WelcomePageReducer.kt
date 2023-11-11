package com.technostore.feature_login.welcome_page.presentation

import com.technostore.arch.mvi.InitialState
import com.technostore.arch.mvi.Reducer
import com.technostore.arch.mvi.State

class WelcomePageReducer : Reducer<State, WelcomePageEvent> {
    override fun reduce(currentState: State, event: WelcomePageEvent): State {
        return InitialState()
    }
}