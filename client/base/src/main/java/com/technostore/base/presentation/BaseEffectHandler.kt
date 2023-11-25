package com.technostore.base.presentation

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.InitialState
import com.technostore.arch.mvi.Store

class BaseEffectHandler(private val appStore: AppStore) :
    EffectHandler<InitialState, BaseEvent> {
    override suspend fun process(
        event: BaseEvent,
        currentState: InitialState,
        store: Store<InitialState, BaseEvent>
    ) {
        when (event) {
            is BaseEvent.Init -> {
                if (appStore.isActive) {
                    store.acceptNews(BaseNews.OpenMainPage)
                    return
                }
                if (appStore.isOnboardingShown) {
                    store.acceptNews(BaseNews.OpenLogin)
                } else {
                    store.acceptNews(BaseNews.OpenOnboarding)
                }
            }
        }
    }

}