package com.technostore.feature_login.confirm_code.presentation

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
class ConfirmationCodeViewModelTest {
    private val store = mockk<Store<ConfirmationCodeState, ConfirmationCodeEvent>>(relaxed = true)
    private val viewModel = ConfirmationCodeViewModel(store)
    private val testDispatcher = StandardTestDispatcher()
    private val code = "12345"

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `check recovery code → send OnConfirmCode event`() = runTest {
        viewModel.checkRecoveryCode(email = TestData.EMAIL, code = code)
        val expectedEvent = ConfirmationCodeUIEvent.OnConfirmCode(
            email = TestData.EMAIL,
            code = code
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }
}