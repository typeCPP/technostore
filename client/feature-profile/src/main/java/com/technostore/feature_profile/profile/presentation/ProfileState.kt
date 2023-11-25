package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.mvi.State

data class ProfileState(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val isLoading: Boolean = false
) : State {
}