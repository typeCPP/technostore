package com.technostore.feature_login.sign_in.presentation

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
class SignInViewModelTest : SignInBaseTest() {
    private val signInViewModel = SignInViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `sign in clicked → send onSignInClikedEvent`() = runTest {
        signInViewModel.signInClicked(email, password)
        advanceUntilIdle()
        coVerify {
            store.dispatch(
                SignInUiEvent.OnSignInClicked(
                    email = email,
                    password = password
                )
            )
        }
    }

    @Test
    fun `forgot password → send OnForgotPasswordClicked event`() = runTest {
        signInViewModel.forgotPasswordClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(SignInUiEvent.OnForgotPasswordClicked) }
    }

    @Test
    fun `registration clicked → send OnRegistrationClicked event`() = runTest {
        signInViewModel.registrationClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(SignInUiEvent.OnRegistrationClicked) }
    }
}