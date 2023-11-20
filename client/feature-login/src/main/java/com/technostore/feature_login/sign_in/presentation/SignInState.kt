package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.State
import com.technostore.feature_login.common_ui.EmailValidation
import com.technostore.feature_login.common_ui.PasswordValidation

data class SignInState(
    val isLoading: Boolean = false,
    val emailValidation: EmailValidation = EmailValidation.SUCCESS,
    val passwordValidation: PasswordValidation = PasswordValidation.SUCCESS
) : State