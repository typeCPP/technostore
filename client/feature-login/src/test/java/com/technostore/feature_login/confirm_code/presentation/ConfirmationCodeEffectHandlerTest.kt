package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ConfirmationCodeEffectHandlerTest {

    private val loginRepository = mockk<LoginRepository> {
        coEvery { checkRecoveryCodeForAccountConfirmations(any(), any()) } returns Result.Error()
    }
    private val effectHandler = ConfirmationCodeEffectHandler(loginRepository)
    private val defaultState = ConfirmationCodeState()
    private val store = mockk<Store<ConfirmationCodeState, ConfirmationCodeEvent>>(relaxed = true)


    @Test
    fun `event OnConfirmCode, Result is error`() = runTest {
        val event = ConfirmationCodeUIEvent.OnConfirmCode(TestData.EMAIL, TestData.PASSWORD)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.StartLoading) }
        coVerify(exactly = 1) {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                code = event.code,
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ConfirmationCodeNews.ShowErrorToast) }
    }

    @Test
    fun `event OnConfirmCode, Result is Success, data == null`() = runTest {
        loginRepository.apply {
            coEvery {
                checkRecoveryCodeForAccountConfirmations(
                    any(),
                    any()
                )
            } returns Result.Success(null)
        }
        val event = ConfirmationCodeUIEvent.OnConfirmCode(TestData.EMAIL, TestData.PASSWORD)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.StartLoading) }
        coVerify(exactly = 1) {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                code = event.code,
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ConfirmationCodeNews.CodeIsInvalid) }
    }

    @Test
    fun `event OnConfirmCode, Result is Success, data != null`() = runTest {
        loginRepository.apply {
            coEvery {
                checkRecoveryCodeForAccountConfirmations(
                    any(),
                    any()
                )
            } returns Result.Success(true)
        }
        val event = ConfirmationCodeUIEvent.OnConfirmCode(TestData.EMAIL, TestData.PASSWORD)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.StartLoading) }
        coVerify(exactly = 1) {
            loginRepository.checkRecoveryCodeForAccountConfirmations(
                code = event.code,
                email = event.email
            )
        }
        coVerify(exactly = 1) { store.dispatch(ConfirmationCodeEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ConfirmationCodeNews.OpenMainPage) }
    }
}