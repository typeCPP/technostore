package com.technostore.feature_login.welcome_page.presentation

import com.technostore.arch.mvi.Store
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomePageViewModelTest {
    private val store = mockk<Store<WelcomePageState, WelcomePageEvent>>(relaxed = true)
    private val viewModel = WelcomePageViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `next button clicked → send OnNextClicked event`() = runTest {
        viewModel.nextClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(WelcomePageEvent.OnNextClicked) }
    }
}