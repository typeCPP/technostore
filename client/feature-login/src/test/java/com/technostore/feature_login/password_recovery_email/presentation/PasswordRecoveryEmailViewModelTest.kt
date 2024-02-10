package com.technostore.feature_login.password_recovery_email.presentation

import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PasswordRecoveryEmailViewModelTest : PasswordRecoveryEmailBaseTest() {
    private val viewModel = PasswordRecoveryEmailViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `next button clicked → send OnNextClicked event`() = runTest {
        viewModel.nextClicked(email)
        advanceUntilIdle()
        coVerify { store.dispatch(PasswordRecoveryEmailUIEvent.OnNextClicked(email)) }
    }
}