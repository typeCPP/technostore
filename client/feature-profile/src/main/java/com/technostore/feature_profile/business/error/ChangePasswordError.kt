package com.technostore.feature_profile.business.error

import com.technostore.arch.result.ErrorType

sealed class ChangePasswordError : ErrorType() {
    data object WrongOldPassword : ChangePasswordError()
}