package com.technostore.feature_login.welcome_page.presentation

import com.technostore.arch.mvi.Reducer

class WelcomePageReducer : Reducer<WelcomePageState, WelcomePageEvent> {
    override fun reduce(currentState: WelcomePageState, event: WelcomePageEvent): WelcomePageState {
        return WelcomePageState()
    }
}