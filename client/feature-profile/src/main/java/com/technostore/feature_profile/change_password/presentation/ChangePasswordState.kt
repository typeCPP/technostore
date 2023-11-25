package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.State

data class ChangePasswordState(
    val isLoading: Boolean = false
) : State