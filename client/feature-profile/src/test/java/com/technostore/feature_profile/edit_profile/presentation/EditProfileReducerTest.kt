package com.technostore.feature_profile.edit_profile.presentation

import org.junit.Assert.assertTrue
import org.junit.Test

class EditProfileReducerTest {
    private val reducer = EditProfileReducer()
    private val defaultState = EditProfileState()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = EditProfileEvent.StartLoading
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = EditProfileEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = true), event)
        assertTrue(!newState.isLoading)
    }
}