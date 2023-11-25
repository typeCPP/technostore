package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.State

data class EditProfileState(
    val name: String = "",
    val lastName: String = "",
    val photo: String = "",
    val isLoading: Boolean = false
) : State