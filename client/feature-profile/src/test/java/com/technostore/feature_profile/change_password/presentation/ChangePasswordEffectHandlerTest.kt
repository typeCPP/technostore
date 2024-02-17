package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.ErrorType
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_profile.business.ProfileRepository
import com.technostore.feature_profile.business.error.ChangePasswordError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ChangePasswordEffectHandlerTest {

    private val profileRepository = mockk<ProfileRepository> {
        coEvery { changePassword(any(), any()) } returns Result.Success()
    }
    private val effectHandler = ChangePasswordEffectHandler(profileRepository)
    private val defaultState = ChangePasswordState()
    private val store = mockk<Store<ChangePasswordState, ChangePasswordEvent>>(relaxed = true)

    @Test
    fun `event OnBackButtonClicked`() = runTest {
        val event = ChangePasswordUiEvent.OnBackButtonClicked

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.OpenPreviousPage) }
    }

    @Test
    fun `event OnChangePasswordClicked, old password is Empty`() = runTest {
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked("", TestData.PASSWORD, TestData.PASSWORD)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.OldPasswordIsEmpty) }
    }

    @Test
    fun `event OnChangePasswordClicked, new password is Empty`() = runTest {
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(TestData.PASSWORD, "", TestData.PASSWORD)

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.NewPasswordIsEmpty) }
    }

    @Test
    fun `event OnChangePasswordClicked, repeat password is Empty`() = runTest {
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(TestData.PASSWORD, TestData.PASSWORD, "")

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.NewRepeatPasswordIsEmpty) }
    }

    @Test
    fun `event OnChangePasswordClicked, repeat password != new password`() = runTest {
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(
                TestData.PASSWORD,
                TestData.PASSWORD,
                "qwe"
            )

        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.PasswordsIsNotEquals) }
    }

    @Test
    fun `event OnChangePasswordClicked without mistakes, Result is success`() = runTest {
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(
                TestData.PASSWORD,
                TestData.PASSWORD,
                TestData.PASSWORD
            )
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {
            profileRepository.changePassword(
                oldPassword = event.oldPassword,
                newPassword = event.newPassword
            )
        }
        coVerify(exactly = 1) { store.dispatch(ChangePasswordEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.OpenPreviousPage) }
    }

    @Test
    fun `event OnChangePasswordClicked without mistakes, Result is null`() = runTest {
        profileRepository.apply {
            coEvery {
                changePassword(
                    any(),
                    any()
                )
            } returns Result.Error(null)
        }
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(
                TestData.PASSWORD,
                TestData.PASSWORD,
                TestData.PASSWORD
            )
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {
            profileRepository.changePassword(
                oldPassword = event.oldPassword,
                newPassword = event.newPassword
            )
        }
        coVerify(exactly = 1) { store.dispatch(ChangePasswordEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.ShowErrorToast) }
    }

    @Test
    fun `event OnChangePasswordClicked without mistakes, Result is error`() = runTest {
        profileRepository.apply {
            coEvery {
                changePassword(
                    any(),
                    any()
                )
            } returns Result.Error(ChangePasswordError.WrongOldPassword)
        }
        val event =
            ChangePasswordUiEvent.OnChangePasswordClicked(
                TestData.PASSWORD,
                TestData.PASSWORD,
                TestData.PASSWORD
            )
        effectHandler.process(event = event, currentState = defaultState, store = store)
        coVerify(exactly = 1) {
            profileRepository.changePassword(
                oldPassword = event.oldPassword,
                newPassword = event.newPassword
            )
        }
        coVerify(exactly = 1) { store.dispatch(ChangePasswordEvent.EndLoading) }
        coVerify(exactly = 1) { store.acceptNews(ChangePasswordNews.WrongOldPassword) }
    }
}