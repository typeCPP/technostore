package com.technostore.feature_login.business.sign_in.error

import com.technostore.arch.result.ErrorType

sealed class SignInError : ErrorType() {
    object ErrorPassword : SignInError()
    object ErrorEmail : SignInError()
}