package com.technostore.feature_profile.profile.presentation

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
class ProfileViewModelTest : ProfileBaseTest() {
    private val viewModel = ProfileViewModel(store)

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init → send Init event`() = runTest {
        viewModel.init()
        advanceUntilIdle()
        coVerify { store.dispatch(ProfileUiEvent.Init) }
    }

    @Test
    fun `password clicked → send OnChangePasswordClicked event`() = runTest {
        viewModel.editPasswordClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProfileUiEvent.OnChangePasswordClicked) }
    }

    @Test
    fun `edit profile clicked → send OnChangeProfileClicked event`() = runTest {
        viewModel.editProfileClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProfileUiEvent.OnChangeProfileClicked) }
    }

    @Test
    fun `logout clicked → send OnLogoutClicked event`() = runTest {
        viewModel.logoutClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProfileUiEvent.OnLogoutClicked) }
    }

    @Test
    fun `order history clicked → send OnOrderHistoryClicked event`() = runTest {
        viewModel.orderHistoryClicked()
        advanceUntilIdle()
        coVerify { store.dispatch(ProfileUiEvent.OnOrderHistoryClicked) }
    }
}