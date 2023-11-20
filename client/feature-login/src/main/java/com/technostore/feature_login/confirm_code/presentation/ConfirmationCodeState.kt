package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.State

data class ConfirmationCodeState(
    val isLoading: Boolean = false
) : State