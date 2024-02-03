package com.technostore.feature_login.business.sign_in.error

import com.technostore.arch.result.ErrorType

sealed class SignInError : ErrorType() {
    data object ErrorPassword : SignInError()
    data object ErrorEmail : SignInError()
}