package com.technostore.feature_login.sign_in.presentation

import com.technostore.arch.mvi.State

data class SignInState(
    val isLoading: Boolean = false,
    val emailValidation: Validation = Validation.SUCCESS,
    val passwordValidation: Validation = Validation.SUCCESS
) : State

enum class Validation {
    SUCCESS, ERROR, EMPTY
}