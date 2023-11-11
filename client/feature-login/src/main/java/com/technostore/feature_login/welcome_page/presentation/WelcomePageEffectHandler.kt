package com.technostore.feature_login.welcome_page.presentation

import android.util.Log
import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.State
import com.technostore.arch.mvi.Store


class WelcomePageEffectHandler : EffectHandler<State, WelcomePageEvent> {

    override suspend fun process(
        event: WelcomePageEvent,
        currentState: State,
        store: Store<State, WelcomePageEvent>,
    ) {
        when (event) {
            WelcomePageEvent.OnNextClicked -> Log.wtf("WelcomePageEffectHandler", "NextClicked")
        }
    }
}