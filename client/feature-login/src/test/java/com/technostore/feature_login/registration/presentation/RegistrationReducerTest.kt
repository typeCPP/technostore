package com.technostore.feature_login.registration.presentation

import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistrationReducerTest : RegistrationBaseTest() {
    private val reducer = RegistrationReducer()
    private val defaultLoadingState = defaultState.copy(isLoading = true)

    /* EmailIsEmpty */
    @Test
    fun `event EmailIsEmpty → stop loading, set email error`() {
        val event = RegistrationEvent.EmailIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.EMPTY)
    }

    /* EmailMaxLength */
    @Test
    fun `event EmailMaxLength → stop loading, set email error`() {
        val event = RegistrationEvent.EmailMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR_MAX_LENGTH)
    }

    /* EmailIsInvalid */
    @Test
    fun `event EmailIsInvalid → stop loading, set email error`() {
        val event = RegistrationEvent.EmailIsInvalid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.ERROR)
    }
    /* EmailExists */

    @Test
    fun `event EmailExists → stop loading, set email error`() {
        val event = RegistrationEvent.EmailExists
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.emailValidation == EmailValidation.EXISTS)
    }

    /* EmailIsValid */
    @Test
    fun `event EmailIsValid → set email success`() {
        val event = RegistrationEvent.EmailIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(newState.emailValidation == EmailValidation.SUCCESS)
    }

    /* FirstPasswordIsEmpty */
    @Test
    fun `event FirstPasswordIsEmpty → stop loading, set first password error`() {
        val event = RegistrationEvent.FirstPasswordIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.EMPTY)
    }

    /* FirstPasswordErrorSymbols */
    @Test
    fun `event FirstPasswordErrorSymbols → stop loading, set first password error`() {
        val event = RegistrationEvent.FirstPasswordErrorSymbols
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_SYMBOL)
    }

    /* FirstPasswordErrorMinLength */
    @Test
    fun `event FirstPasswordErrorMinLength → stop loading, set first password error`() {
        val event = RegistrationEvent.FirstPasswordErrorMinLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_MIN_LENGTH)
    }

    /* FirstPasswordErrorMaxLength */
    @Test
    fun `event FirstPasswordErrorMaxLength → stop loading, set first password error`() {
        val event = RegistrationEvent.FirstPasswordErrorMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_MAX_LENGTH)
    }

    /* FirstPasswordIsValid */
    @Test
    fun `event FirstPasswordIsValid → set first password is valid`() {
        val event = RegistrationEvent.FirstPasswordIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(newState.firstPasswordValidation == PasswordValidation.SUCCESS)
    }

    /* SecondPasswordIsEmpty */
    @Test
    fun `event SecondPasswordIsEmpty → stop loading, set second password error`() {
        val event = RegistrationEvent.SecondPasswordIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.EMPTY)
    }

    /* SecondPasswordErrorSymbols */
    @Test
    fun `event SecondPasswordErrorSymbols → stop loading, set second password error`() {
        val event = RegistrationEvent.SecondPasswordErrorSymbols
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_SYMBOL)
    }

    /* SecondPasswordErrorMinLength */
    @Test
    fun `event SeconfPasswordErrorMinLength → stop loading, set second password error`() {
        val event = RegistrationEvent.SecondPasswordErrorMinLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_MIN_LENGTH)
    }

    /* SecondPasswordErrorMaxLength */
    @Test
    fun `event SecondPasswordErrorMaxLength → stop loading, set second password error`() {
        val event = RegistrationEvent.SecondPasswordErrorMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_MAX_LENGTH)
    }

    /* SecondPasswordIsValid */
    @Test
    fun `event SecondPasswordIsValid → set second password is valid`() {
        val event = RegistrationEvent.SecondPasswordIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.SUCCESS)
    }

    /* PasswordsAreBroken */
    @Test
    fun `event PasswordsAreBroken → set passwords error`() {
        val event = RegistrationEvent.PasswordsAreBroken
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.DIFFERENT)
    }

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = RegistrationEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = RegistrationEvent.EndLoading
        val newState = reducer.reduce(defaultLoadingState, event)
        assertTrue(!newState.isLoading)
    }

    @Test
    fun `ui event → return current state`() {
        val event = RegistrationUiEvent.OnLoginClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}