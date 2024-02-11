package com.technostore.feature_login.password_recovery.presentation

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
class PasswordRecoveryViewModelTest : PasswordRecoveryBaseTest() {
    private val viewModel = PasswordRecoveryViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `next button clicked → send OnNextClicked event`() = runTest {
        viewModel.nextClicked(firstPassword = firstPassword, secondPassword = secondPassword)
        advanceUntilIdle()
        coVerify {
            store.dispatch(
                PasswordRecoveryUIEvent.OnNextClicked(
                    firstPassword = firstPassword,
                    secondPassword = secondPassword
                )
            )
        }
    }
}
