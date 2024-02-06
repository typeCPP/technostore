package com.technostore.feature_profile.presentation

import com.technostore.feature_profile.edit_profile.presentation.EditProfileEvent
import com.technostore.feature_profile.edit_profile.presentation.EditProfileReducer
import com.technostore.feature_profile.edit_profile.presentation.EditProfileState
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