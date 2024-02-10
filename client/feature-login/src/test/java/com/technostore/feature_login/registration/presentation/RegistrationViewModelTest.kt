package com.technostore.feature_login.registration.presentation

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
class RegistrationViewModelTest : RegistrationBaseTest() {
    private val viewModel = RegistrationViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `registration clicked → send onSRegistrationClickedEvent`() = runTest {
        viewModel.registrationClicked(
            email = email,
            firstPassword = password,
            secondPassword = password
        )
        advanceUntilIdle()
        coVerify {
            store.dispatch(
                RegistrationUiEvent.OnRegistrationClicked(
                    email = email,
                    firstPassword = password,
                    secondPassword = password
                )
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login clicked → send OnSignInClicked event`() = runTest {
        viewModel.loginClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(RegistrationUiEvent.OnLoginClicked) }
    }
}