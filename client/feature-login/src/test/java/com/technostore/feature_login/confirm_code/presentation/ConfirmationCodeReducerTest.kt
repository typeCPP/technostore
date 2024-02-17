package com.technostore.feature_login.confirm_code.presentation

import com.technostore.common_test.TestData
import org.junit.Assert
import org.junit.Test

class ConfirmationCodeReducerTest {
    private val confirmationCodeReducer = ConfirmationCodeReducer()
    private val defaultState = ConfirmationCodeState()

    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = ConfirmationCodeEvent.StartLoading
        val newState = confirmationCodeReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(newState.isLoading)
    }

    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = ConfirmationCodeEvent.EndLoading
        val newState = confirmationCodeReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(!newState.isLoading)
    }

    @Test
    fun ` UI event → do nothing`() {
        val event = ConfirmationCodeUIEvent.OnConfirmCode(TestData.EMAIL, TestData.CODE)
        val newState = confirmationCodeReducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }
}