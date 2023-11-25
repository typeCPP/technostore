package com.technostore.feature_profile.change_password.presentation

import com.technostore.arch.mvi.News

sealed class ChangePasswordNews : News() {
    data object WrongOldPassword : ChangePasswordNews()
    data object ShowErrorToast : ChangePasswordNews()
    data object OpenPreviousPage : ChangePasswordNews()
    data object OldPasswordIsEmpty : ChangePasswordNews()
    data object NewPasswordIsEmpty : ChangePasswordNews()
    data object NewRepeatPasswordIsEmpty : ChangePasswordNews()
    data object PasswordsIsNotEquals : ChangePasswordNews()
}