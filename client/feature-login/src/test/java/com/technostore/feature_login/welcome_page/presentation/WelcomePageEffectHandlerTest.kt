package com.technostore.feature_login.welcome_page.presentation

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.Store
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WelcomePageEffectHandlerTest {

    private val appStore = mockk<AppStore>(relaxed = true) {
        coEvery { isOnboardingShown } returns false
    }
    private val effectHandler = WelcomePageEffectHandler(appStore)
    private val defaultState = WelcomePageState()
    private val store = mockk<Store<WelcomePageState, WelcomePageEvent>>(relaxed = true)

    @Test
    fun `event OnNextClicked`() = runTest {
        val event = WelcomePageEvent.OnNextClicked
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(WelcomePageNews.OpenLoginPage) }
        coVerify(exactly = 1) { appStore.isOnboardingShown = true }
    }
}