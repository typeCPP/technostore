package com.technostore.feature_profile.change_password.presentation

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
class ChangePasswordViewModelTest : ChangePasswordBaseTest() {
    private val viewModel = ChangePasswordViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `change password clicked → send OnChangePasswordClicked event`() = runTest {
        viewModel.onChangedClicked(
            oldPassword = password,
            newPassword = password,
            newRepeatPassword = password
        )
        val expectedEvent = ChangePasswordUiEvent.OnChangePasswordClicked(
            oldPassword = password,
            newPassword = password,
            newRepeatPassword = password
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }

    @Test
    fun `back clicked → send OnBackButtonClicked event`() = runTest {
        viewModel.onBackClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ChangePasswordUiEvent.OnBackButtonClicked) }
    }
}