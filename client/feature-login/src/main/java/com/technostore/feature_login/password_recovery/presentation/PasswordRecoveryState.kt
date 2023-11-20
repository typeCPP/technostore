package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_login.common_ui.PasswordValidation

data class PasswordRecoveryState(
    val isLoading: Boolean = false,
    val firstPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS,
    val secondPasswordValidation: PasswordValidation = PasswordValidation.SUCCESS
) : State