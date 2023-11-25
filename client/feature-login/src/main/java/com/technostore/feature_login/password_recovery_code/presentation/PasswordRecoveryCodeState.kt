package com.technostore.feature_login.password_recovery_code.presentation

import com.technostore.arch.mvi.State

data class PasswordRecoveryCodeState(
    val isLoading: Boolean = false
) : State