package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PasswordRecoveryCodeEffectHandlerTest {

    private val loginRepository = mockk<LoginRepository>(relaxed = true) {
        coEvery { sendCodeForRecoveryPassword(any()) } returns Result.Success()
        coEvery { checkRecoveryCode(any(), any()) } returns Result.Error()
    }
    private val effectHandler = PasswordRecoveryCodeEffectHandler(loginRepository)
    private val defaultState = PasswordRecoveryCodeState()
    private val store =
        mockk<Store<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent>>(relaxed = true)

    @Test
    fun `event OnRepeatClicked, result is Success`() = runTest {
        val event =
            PasswordRecoveryCodeUiEvent.OnRepeatClicked(TestData.EMAIL)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {
            loginRepository.sendCodeForRecoveryPassword(
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.EndLoading) }
    }

    @Test
    fun `event OnRepeatClicked, result is Error`() = runTest {
        loginRepository.apply { coEvery { sendCodeForRecoveryPassword(any()) } returns Result.Error() }
        val event =
            PasswordRecoveryCodeUiEvent.OnRepeatClicked(TestData.EMAIL)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {
            loginRepository.sendCodeForRecoveryPassword(
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryCodeNews.ShowErrorToast) }
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.EndLoading) }
    }

    @Test
    fun `event OnNextClicked, code length more then 255`() = runTest {
        val event =
            PasswordRecoveryCodeUiEvent.OnNextClicked(TestData.EMAIL, "1".repeat(256))

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryCodeNews.CodeErrorLength) }
    }

    @Test
    fun `event OnNextClicked, code length less then 255, Result is Error`() = runTest {
        val event =
            PasswordRecoveryCodeUiEvent.OnNextClicked(TestData.EMAIL, TestData.CODE)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.StartLoading) }
        coVerify(exactly = 1) {
            loginRepository.checkRecoveryCode(
                code = event.code,
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryCodeNews.ShowErrorToast) }
    }

    @Test
    fun `event OnNextClicked, code length less then 255, Result is Success, data == null`() =
        runTest {
            loginRepository.apply {
                coEvery {
                    checkRecoveryCode(
                        any(),
                        any()
                    )
                } returns Result.Success(null)
            }
            val event =
                PasswordRecoveryCodeUiEvent.OnNextClicked(TestData.EMAIL, TestData.CODE)

            effectHandler.process(event = event, currentState = defaultState, store = store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.StartLoading) }
            coVerify(exactly = 1) {
                loginRepository.checkRecoveryCode(
                    code = event.code,
                    email = event.email
                )
            }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryCodeNews.CodeIsInvalid) }
        }

    @Test
    fun `event OnNextClicked, code length less then 255, Result is Success, data != null`() =
        runTest {
            loginRepository.apply {
                coEvery {
                    checkRecoveryCode(
                        any(),
                        any()
                    )
                } returns Result.Success(true)
            }
            val event =
                PasswordRecoveryCodeUiEvent.OnNextClicked(TestData.EMAIL, TestData.CODE)

            effectHandler.process(event = event, currentState = defaultState, store = store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.StartLoading) }
            coVerify(exactly = 1) {
                loginRepository.checkRecoveryCode(
                    code = event.code,
                    email = event.email
                )
            }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryCodeEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryCodeNews.OpenPasswordRecoveryPage) }
        }
}