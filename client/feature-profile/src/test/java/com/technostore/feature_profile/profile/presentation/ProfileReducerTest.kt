package com.technostore.feature_profile.profile.presentation

import com.technostore.common_test.TestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileReducerTest : ProfileBaseTest() {
    private val reducer = ProfileReducer()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = ProfileEvent.StartLoading
        val currentState = defaultState.copy(isLoading = false)
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = ProfileEvent.EndLoading
        val currentState = defaultState.copy(isLoading = true)
        val newState = reducer.reduce(currentState, event)
        assertTrue(!newState.isLoading)
    }

    /* ProfileLoaded */
    @Test
    fun `event ProfileLoaded → set data`() {
        val event = ProfileEvent.ProfileLoaded(
            name = TestData.NAME,
            lastName = TestData.LAST_NAME,
            email = TestData.EMAIL,
            image = TestData.USER_PHOTO_LINK
        )
        val currentState = defaultState.copy(
            name = "",
            lastName = "",
            email = "",
            image = ""
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState.name == TestData.NAME)
        assertTrue(newState.lastName == TestData.LAST_NAME)
        assertTrue(newState.email == TestData.EMAIL)
        assertTrue(newState.image == TestData.USER_PHOTO_LINK)
    }

    /* UI event */
    @Test
    fun `UI event → return current state`() {
        val event = ProfileUiEvent.OnChangeProfileClicked
        val newState = reducer.reduce(defaultState, event)
        assertEquals(defaultState, newState)
    }
}