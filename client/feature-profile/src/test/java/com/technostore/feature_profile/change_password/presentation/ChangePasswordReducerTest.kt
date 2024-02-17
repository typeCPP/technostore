package com.technostore.feature_profile.change_password.presentation

import org.junit.Assert
import org.junit.Test

class ChangePasswordReducerTest {

    private val changePasswordReducer = ChangePasswordReducer()
    private val defaultState = ChangePasswordState()

    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = ChangePasswordEvent.StartLoading
        val newState = changePasswordReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(newState.isLoading == true)
    }

    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = ChangePasswordEvent.EndLoading
        val newState = changePasswordReducer.reduce(defaultState.copy(isLoading = true), event)
        Assert.assertTrue(newState.isLoading == false)
    }

    @Test
    fun ` UI event → do nothing`() {
        val event = ChangePasswordUiEvent.OnBackButtonClicked
        val newState = changePasswordReducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }
}