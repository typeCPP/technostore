package com.technostore.feature_login.password_recovery_email.presentation

import com.technostore.feature_login.common_ui.EmailValidation
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordRecoveryEmailReducerTest : PasswordRecoveryEmailBaseTest() {
    private val reducer = PasswordRecoveryEmailReducer()
    private val defaultLoadingState = defaultState.copy(isLoading = true)

    /* EmailIsEmpty */
    @Test
    fun `event EmailIsEmpty → stop loading, set email error`() {
        val event = PasswordRecoveryEmailEvent.EmailIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.EMPTY)
    }

    /* EmailMaxLength */
    @Test
    fun `event EmailMaxLength → stop loading, set email error`() {
        val event = PasswordRecoveryEmailEvent.EmailMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR_MAX_LENGTH)
    }

    /* EmailIsInvalid */
    @Test
    fun `event EmailIsInvalid → stop loading, set email error`() {
        val event = PasswordRecoveryEmailEvent.EmailIsInvalid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR)
    }
    /* EmailNotExists */

    @Test
    fun `event EmailNotExists → stop loading, set email error`() {
        val event = PasswordRecoveryEmailEvent.EmailNotExists
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.NOT_EXISTS)
    }

    /* EmailIsValid */
    @Test
    fun `event EmailIsValid → set email success`() {
        val event = PasswordRecoveryEmailEvent.EmailIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(newState.emailValidation == EmailValidation.SUCCESS)
    }

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = PasswordRecoveryEmailEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = PasswordRecoveryEmailEvent.EndLoading
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading)
    }

    @Test
    fun `ui event → return current state`() {
        val event = PasswordRecoveryEmailUIEvent.OnNextClicked(email=email)
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}