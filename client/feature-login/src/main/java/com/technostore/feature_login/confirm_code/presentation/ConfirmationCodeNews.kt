package com.technostore.feature_login.confirm_code.presentation

import com.technostore.arch.mvi.News

sealed class ConfirmationCodeNews : News() {
    data object ShowErrorToast : ConfirmationCodeNews()
    data object OpenMainPage : ConfirmationCodeNews()
    data object CodeIsInvalid : ConfirmationCodeNews()
}