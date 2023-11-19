package com.technostore.feature_login.welcome_page.presentation

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.State
import com.technostore.arch.mvi.Store
import com.technostore.feature_login.welcome_page.presentation.WelcomePageNews.OpenLoginPage


class WelcomePageEffectHandler(private val appStore: AppStore) :
    EffectHandler<State, WelcomePageEvent> {

    override suspend fun process(
        event: WelcomePageEvent,
        currentState: State,
        store: Store<State, WelcomePageEvent>,
    ) {
        when (event) {
            WelcomePageEvent.OnNextClicked -> {
                appStore.isOnboardingShown = true
                store.acceptNews(OpenLoginPage)
            }
        }
    }
}