package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.common_test.TestData
import org.junit.Assert
import org.junit.Test

class PasswordRecoveryCodeReducerTest {
    private val passwordRecoveryCodeReducer = PasswordRecoveryCodeReducer()
    private val defaultState = PasswordRecoveryCodeState()

    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = PasswordRecoveryCodeEvent.StartLoading
        val newState = passwordRecoveryCodeReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(newState.isLoading == true)
    }

    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = PasswordRecoveryCodeEvent.EndLoading
        val newState = passwordRecoveryCodeReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(newState.isLoading == false)
    }

    @Test
    fun ` UI event → do nothing`() {
        val event = PasswordRecoveryCodeUiEvent.OnRepeatClicked(TestData.EMAIL)
        val newState = passwordRecoveryCodeReducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }
}