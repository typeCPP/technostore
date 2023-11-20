package com.technostore.feature_login.common_ui

const val PASSWORD_REGEX = "^((?=.*[A-Za-z\\d]))[A-Za-z\\d]{1,}\$"

enum class PasswordValidation {
    SUCCESS,
    ERROR_SYMBOL,
    ERROR_MIN_LENGTH,
    ERROR_MAX_LENGTH,
    EMPTY,
    DIFFERENT,
    INCORRECT_PASSWORD
}