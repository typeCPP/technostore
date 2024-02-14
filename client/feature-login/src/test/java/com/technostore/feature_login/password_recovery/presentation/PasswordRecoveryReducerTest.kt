package com.technostore.feature_login.password_recovery.presentation

import com.technostore.feature_login.common_ui.PasswordValidation
import org.junit.Assert
import org.junit.Test

class PasswordRecoveryReducerTest : PasswordRecoveryBaseTest() {
    private val reducer = PasswordRecoveryReducer()
    private val defaultLoadingState = defaultState.copy(isLoading = true)

    /* FirstPasswordIsEmpty */
    @Test
    fun `event FirstPasswordIsEmpty → stop loading, set first password error`() {
        val event = PasswordRecoveryEvent.FirstPasswordIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.EMPTY)
    }

    /* FirstPasswordErrorSymbols */
    @Test
    fun `event FirstPasswordErrorSymbols → stop loading, set first password error`() {
        val event = PasswordRecoveryEvent.FirstPasswordErrorSymbols
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_SYMBOL)
    }

    /* FirstPasswordErrorMinLength */
    @Test
    fun `event FirstPasswordErrorMinLength → stop loading, set first password error`() {
        val event = PasswordRecoveryEvent.FirstPasswordErrorMinLength
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_MIN_LENGTH)
    }

    /* FirstPasswordErrorMaxLength */
    @Test
    fun `event FirstPasswordErrorMaxLength → stop loading, set first password error`() {
        val event = PasswordRecoveryEvent.FirstPasswordErrorMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.firstPasswordValidation == PasswordValidation.ERROR_MAX_LENGTH)
    }

    /* FirstPasswordIsValid */
    @Test
    fun `event FirstPasswordIsValid → set first password is valid`() {
        val event = PasswordRecoveryEvent.FirstPasswordIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(newState.firstPasswordValidation == PasswordValidation.SUCCESS)
    }

    /* SecondPasswordIsEmpty */
    @Test
    fun `event SecondPasswordIsEmpty → stop loading, set second password error`() {
        val event = PasswordRecoveryEvent.SecondPasswordIsEmpty
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.EMPTY)
    }

    /* SecondPasswordErrorSymbols */
    @Test
    fun `event SecondPasswordErrorSymbols → stop loading, set second password error`() {
        val event = PasswordRecoveryEvent.SecondPasswordErrorSymbols
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_SYMBOL)
    }

    /* SecondPasswordErrorMinLength */
    @Test
    fun `event SeconfPasswordErrorMinLength → stop loading, set second password error`() {
        val event = PasswordRecoveryEvent.SecondPasswordErrorMinLength
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_MIN_LENGTH)
    }

    /* SecondPasswordErrorMaxLength */
    @Test
    fun `event SecondPasswordErrorMaxLength → stop loading, set second password error`() {
        val event = PasswordRecoveryEvent.SecondPasswordErrorMaxLength
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.ERROR_MAX_LENGTH)
    }

    /* SecondPasswordIsValid */
    @Test
    fun `event SecondPasswordIsValid → set second password is valid`() {
        val event = PasswordRecoveryEvent.SecondPasswordIsValid
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.SUCCESS)
    }

    /* PasswordsAreBroken */
    @Test
    fun `event PasswordsAreBroken → set passwords error`() {
        val event = PasswordRecoveryEvent.PasswordsAreBroken
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading && newState.secondPasswordValidation == PasswordValidation.DIFFERENT)
    }

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = PasswordRecoveryEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        Assert.assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = PasswordRecoveryEvent.EndLoading
        val newState = reducer.reduce(defaultLoadingState, event)
        Assert.assertTrue(!newState.isLoading)
    }

    @Test
    fun `ui event → return current state`() {
        val event = PasswordRecoveryUIEvent.OnNextClicked(firstPassword, secondPassword)
        val newState = reducer.reduce(defaultState, event)
        Assert.assertTrue(newState == defaultState)
    }
}