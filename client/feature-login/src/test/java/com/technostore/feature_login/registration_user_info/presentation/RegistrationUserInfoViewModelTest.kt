package com.technostore.feature_login.registration_user_info.presentation

import com.technostore.arch.mvi.Store
import com.technostore.common_test.TestData
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationUserInfoViewModelTest {
    private val store =
        mockk<Store<RegistrationUserInfoState, RegistrationUserInfoEvent>>(relaxed = true)
    private val viewModel = RegistrationUserInfoViewModel(store)
    private val testDispatcher = StandardTestDispatcher()
    private val password = "password123"

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `image changed → send OnImageChanged event`() = runTest {
        viewModel.imageChanged(null)
        advanceUntilIdle()
        coVerify { store.dispatch(RegistrationUserInfoUiEvent.OnImageChanged(null)) }
    }

    @Test
    fun `registration clicked → send OnRegistrationClicked event`() = runTest {
        val byteArray = byteArrayOf(Byte.MIN_VALUE)
        viewModel.registrationClicked(
            byteArray = byteArray,
            email = TestData.EMAIL,
            password = password,
            name = TestData.NAME,
            lastName = TestData.LAST_NAME
        )
        val expectedEvent = RegistrationUserInfoUiEvent.OnRegistrationClicked(
            byteArray = byteArray,
            email = TestData.EMAIL,
            password = password,
            name = TestData.NAME,
            lastName = TestData.LAST_NAME
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }
}