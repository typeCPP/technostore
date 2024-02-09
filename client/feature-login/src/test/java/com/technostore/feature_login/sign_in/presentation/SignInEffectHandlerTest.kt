package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.business.sign_in.error.SignInError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SignInEffectHandlerTest : SignInBaseTest() {
    private val loginRepository = mockk<LoginRepository> {
        coEvery { signIn(any(), any()) } returns Result.Success()
    }
    private val effectHandler = SignInEffectHandler(
        loginRepository = loginRepository
    )
    private val store = mockk<Store<SignInState, SignInEvent>>(relaxed = true)
    private val notValidEmail = "testtesTemail"

    /* OnSignInClicked */

    @Test
    fun `event OnSignInClicked → email valid, passwords is valid start loading, sign in return success → open main page`() =
        runTest {
            val event = SignInUiEvent.OnSignInClicked(
                email = email,
                password = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
            coVerify(exactly = 1) {
                loginRepository.signIn(
                    email = trimEmail,
                    password = password
                )
            }
            coVerify(exactly = 1) { store.acceptNews(SignInNews.OpenMainPage) }
        }

    @Test
    fun `event OnSignInClicked → email valid, passwords is valid start loading, sign in return error imail → stop loading, show error`() =
        runTest {
            loginRepository.apply {
                coEvery { signIn(any(), any()) } returns Result.Error(SignInError.ErrorEmail)
            }
            val event = SignInUiEvent.OnSignInClicked(
                email = email,
                password = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
            coVerify(exactly = 1) {
                loginRepository.signIn(
                    email = trimEmail,
                    password = password
                )
            }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailNotExists) }
        }

    @Test
    fun `event OnSignInClicked → email valid, passwords is valid, start loading, sign in return error password → stop loading, show error`() =
        runTest {
            loginRepository.apply {
                coEvery { signIn(any(), any()) } returns Result.Error(SignInError.ErrorPassword)
            }
            val event = SignInUiEvent.OnSignInClicked(
                email = email,
                password = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
            coVerify(exactly = 1) {
                loginRepository.signIn(
                    email = trimEmail,
                    password = password
                )
            }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordInvalid) }
        }

    @Test
    fun `event OnSignInClicked → email valid, passwords is valid, start loading, sign in return error → stop loading, show error toast`() =
        runTest {
            loginRepository.apply {
                coEvery { signIn(any(), any()) } returns Result.Error()
            }
            val event = SignInUiEvent.OnSignInClicked(
                email = email,
                password = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
            coVerify(exactly = 1) {
                loginRepository.signIn(
                    email = trimEmail,
                    password = password
                )
            }
            coVerify(exactly = 1) { store.dispatch(SignInEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(SignInNews.ShowErrorToast) }
        }

    @Test
    fun `event OnSignInClicked → password is more than 255 symbols → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = email,
            password = password.repeat(255),
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordErrorMaxLength) }
    }

    @Test
    fun `event OnSignInClicked → password is empty → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = email,
            password = "",
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsEmpty) }
    }

    @Test
    fun `event OnSignInClicked → password is null → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = email,
            password = null,
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsValid) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsEmpty) }
    }

    @Test
    fun `event OnSignInClicked → email has invalid symbols → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = notValidEmail,
            password = password,
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsInvalid) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
    }

    @Test
    fun `event OnSignInClicked → email have more than 255 symbols → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = notValidEmail.repeat(255),
            password = password,
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailMaxLength) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
    }

    @Test
    fun `event OnSignInClicked → email is empty → show error`() = runTest {
        val event = SignInUiEvent.OnSignInClicked(
            email = "  ",
            password = password,
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(SignInEvent.EmailIsEmpty) }
        coVerify(exactly = 1) { store.dispatch(SignInEvent.PasswordIsValid) }
    }

    /* OnRegistrationClicked */
    @Test
    fun `event OnRegistrationClicked → open registration page`() = runTest {
        val event = SignInUiEvent.OnRegistrationClicked

        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(SignInNews.OpenRegistrationPage) }
    }

    /* OnForgotPasswordClicked */
    @Test
    fun `event OnForgotPasswordClicked → open password recovery page`() = runTest {
        val event = SignInUiEvent.OnForgotPasswordClicked

        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(SignInNews.OpenPasswordRecoveryPage) }
    }
}