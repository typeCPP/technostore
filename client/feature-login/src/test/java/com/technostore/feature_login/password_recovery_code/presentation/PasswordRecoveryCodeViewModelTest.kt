package com.technostore.feature_login.password_recovery_code.presentation

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
class PasswordRecoveryCodeViewModelTest : PasswordRecoveryCodeBaseTest() {
    private val viewModel = PasswordRecoveryCodeViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `next button clicked → send OnNextClicked event`() = runTest {
        viewModel.onNextClicked(email = email, code = code)
        advanceUntilIdle()
        coVerify {
            store.dispatch(
                PasswordRecoveryCodeUiEvent.OnNextClicked(
                    email = email,
                    code = code
                )
            )
        }
    }

    @Test
    fun `send code again clicked → send OnRepeatClicked event`() = runTest {
        viewModel.onRepeatClicked(email)
        advanceUntilIdle()
        coVerify { store.dispatch(PasswordRecoveryCodeUiEvent.OnRepeatClicked(email)) }
    }
}
