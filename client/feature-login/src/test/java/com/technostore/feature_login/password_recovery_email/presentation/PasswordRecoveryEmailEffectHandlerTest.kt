package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PasswordRecoveryEmailEffectHandlerTest : PasswordRecoveryEmailBaseTest() {
    private val loginRepository = mockk<LoginRepository> {
        coEvery { checkEmailExists(any()) } returns Result.Success(true)
        coEvery { sendCodeForRecoveryPassword(any()) } returns Result.Success()
    }
    private val effectHandler = PasswordRecoveryEmailEffectHandler(
        loginRepository = loginRepository
    )
    private val store =
        mockk<Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent>>(relaxed = true)
    private val notValidEmail = "testtesTemail"

    /* OnNextClicked */
    @Test
    fun `event OnNextClicked → email valid start loading, check email return exists→ send code return success, stop loading → open code page`() =
        runTest {
            val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email = email)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { loginRepository.sendCodeForRecoveryPassword(email = trimEmail) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EndLoading) }
            coVerify(exactly = 1) {
                store.acceptNews(
                    PasswordRecoveryEmailNews.OpenCodePage(
                        trimEmail
                    )
                )
            }
        }

    @Test
    fun `event OnNextClicked → email valid start loading, check email return exists →send code return error, stop loading → show error toast`() =
        runTest {
            loginRepository.apply {
                coEvery { sendCodeForRecoveryPassword(any()) } returns Result.Error()
            }
            val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email = email)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { loginRepository.sendCodeForRecoveryPassword(email = trimEmail) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryEmailNews.ShowErrorToast) }
        }

    @Test
    fun `event OnNextClicked → email valid start loading, check email return success wothout body → stop loading, set error`() =
        runTest {
            loginRepository.apply {
                coEvery { checkEmailExists(any()) } returns Result.Success()
            }
            val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email = email)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailNotExists) }
        }

    @Test
    fun `event OnNextClicked → email valid start loading, check email return does not exists → stop loading, set error`() =
        runTest {
            loginRepository.apply {
                coEvery { checkEmailExists(any()) } returns Result.Success(false)
            }
            val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email = email)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EndLoading) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailNotExists) }
        }

    @Test
    fun `event OnNextClicked → email valid start loading, check email return error → stop loading, show error toast`() =
        runTest {
            loginRepository.apply {
                coEvery { checkEmailExists(any()) } returns Result.Error()
            }
            val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email = email)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryEmailNews.ShowErrorToast) }
        }

    @Test
    fun `event OnNextClicked → email has invalid symbols → show error`() = runTest {
        val event = PasswordRecoveryEmailUIEvent.OnNextClicked(notValidEmail)
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsInvalid) }
    }

    @Test
    fun `event OnNextClicked → email have more than 255 symbols → show error`() = runTest {
        val event = PasswordRecoveryEmailUIEvent.OnNextClicked(notValidEmail.repeat(255))
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailMaxLength) }
    }

    @Test
    fun `event OnNextClicked → email is empty → show error`() = runTest {
        val event = PasswordRecoveryEmailUIEvent.OnNextClicked("  ")
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEmailEvent.EmailIsEmpty) }
    }
}