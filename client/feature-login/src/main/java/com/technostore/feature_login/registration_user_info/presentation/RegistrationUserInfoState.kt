package com.technostore.feature_login.registration_user_info.presentation

import com.technostore.arch.mvi.State

data class RegistrationUserInfoState(
    val isLoading: Boolean = false,
    val firstNameValidation: NameValidation = NameValidation.SUCCESS,
    val lastNameValidation: NameValidation = NameValidation.SUCCESS
) : State


enum class NameValidation {
    SUCCESS,
    EMPTY,
    ERROR
}