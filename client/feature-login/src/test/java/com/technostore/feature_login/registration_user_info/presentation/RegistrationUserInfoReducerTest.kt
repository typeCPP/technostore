package com.technostore.feature_login.registration_user_info.presentation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RegistrationUserInfoReducerTest {
    private val reducer = RegistrationUserInfoReducer()
    private val defaultState = RegistrationUserInfoState()

    /* NameIsValid */
    @Test
    fun `event NameIsValid → set name validation success`() {
        val event = RegistrationUserInfoEvent.NameIsValid
        val newState =
            reducer.reduce(defaultState.copy(firstNameValidation = NameValidation.ERROR), event)
        assertEquals(newState.firstNameValidation, NameValidation.SUCCESS)
    }

    /* NameIsEmpty */
    @Test
    fun `event NameIsEmpty → set name validation error`() {
        val event = RegistrationUserInfoEvent.NameIsEmpty
        val newState =
            reducer.reduce(defaultState.copy(firstNameValidation = NameValidation.ERROR), event)
        assertEquals(newState.firstNameValidation, NameValidation.EMPTY)
    }

    /* NameErrorLength */
    @Test
    fun `event NameErrorLength → set name validation error`() {
        val event = RegistrationUserInfoEvent.NameErrorLength
        val currentState = defaultState.copy(
            firstNameValidation = NameValidation.SUCCESS,
            isLoading = true
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.firstNameValidation == NameValidation.ERROR && !newState.isLoading)
    }

    /* LastNameIsValid */
    @Test
    fun `event LastNameIsValid → set last name validation success`() {
        val event = RegistrationUserInfoEvent.LastNameIsValid
        val newState =
            reducer.reduce(defaultState.copy(lastNameValidation = NameValidation.ERROR), event)
        assertEquals(newState.lastNameValidation, NameValidation.SUCCESS)
    }

    /* LastNameIsEmpty */
    @Test
    fun `event LastNameIsEmpty → set last name validation error`() {
        val event = RegistrationUserInfoEvent.LastNameIsEmpty
        val newState =
            reducer.reduce(defaultState.copy(lastNameValidation = NameValidation.ERROR), event)
        assertEquals(newState.lastNameValidation, NameValidation.EMPTY)
    }

    /* LastNameErrorLength */
    @Test
    fun `event LastNameErrorLength → set last name validation error`() {
        val event = RegistrationUserInfoEvent.LastNameErrorLength
        val currentState = defaultState.copy(
            lastNameValidation = NameValidation.SUCCESS,
            isLoading = true
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.lastNameValidation == NameValidation.ERROR && !newState.isLoading)
    }

    /* StartLoading */
    @Test
    fun `event StartLoading → set is loading`() {
        val event = RegistrationUserInfoEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → clear is loading`() {
        val event = RegistrationUserInfoEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = true), event)
        assertTrue(!newState.isLoading)
    }

    @Test
    fun `ui event → return current state`() {
        val event = RegistrationUserInfoUiEvent.OnImageChanged(null)
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}