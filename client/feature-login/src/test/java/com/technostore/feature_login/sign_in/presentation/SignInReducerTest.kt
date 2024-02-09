package com.technostore.feature_login.sign_in.presentation

import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation
import org.junit.Assert.assertTrue
import org.junit.Test

class SignInReducerTest : SignInBaseTest() {
    private val reducer = SignInReducer()
    private val defaultLoadingState = defaultState.copy(isLoading = true)

    /* EmailIsEmpty */
    @Test
    fun `event EmailIsEmpty → stop loading, set email error`() {
        val event = SignInEvent.EmailIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.EMPTY)
    }

    /* EmailMaxLength */
    @Test
    fun `event EmailMaxLength → stop loading, set email error`() {
        val event = SignInEvent.EmailMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR_MAX_LENGTH)
    }

    /* EmailIsInvalid */
    @Test
    fun `event EmailIsInvalid → stop loading, set email error`() {
        val event = SignInEvent.EmailIsInvalid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR)
    }
    /* EmailNotExists */

    @Test
    fun `event EmailNotExists → stop loading, set email error`() {
        val event = SignInEvent.EmailNotExists
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.NOT_EXISTS)
    }

    /* EmailIsValid */
    @Test
    fun `event EmailIsValid → set email success`() {
        val event = SignInEvent.EmailIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(newState.emailValidation == EmailValidation.SUCCESS)
    }

    /* PasswordIsEmpty */
    @Test
    fun `event PasswordIsEmpty → stop loading, set password error`() {
        val event = SignInEvent.PasswordIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.passwordValidation == PasswordValidation.EMPTY)
    }

    /* PasswordErrorMaxLength */
    @Test
    fun `event PasswordErrorMaxLength → stop loading, set password error`() {
        val event = SignInEvent.PasswordErrorMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.passwordValidation == PasswordValidation.ERROR_MAX_LENGTH)
    }

    /* PasswordInvalid */
    @Test
    fun `event PasswordInvalid → stop loading, set password error`() {
        val event = SignInEvent.PasswordInvalid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.passwordValidation == PasswordValidation.INCORRECT_PASSWORD)
    }

    /* PasswordIsValid */
    @Test
    fun `event PasswordIsValid → set password is valid`() {
        val event = SignInEvent.PasswordIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(newState.passwordValidation == PasswordValidation.SUCCESS)
    }

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = SignInEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = SignInEvent.EndLoading
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading)
    }

    @Test
    fun `ui event → return current state`() {
        val event = SignInUiEvent.OnRegistrationClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}