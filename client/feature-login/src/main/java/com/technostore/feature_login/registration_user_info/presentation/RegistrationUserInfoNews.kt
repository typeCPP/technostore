package com.technostore.feature_login.registration_user_info.presentation

import android.net.Uri
import com.technostore.arch.mvi.News

sealed class RegistrationUserInfoNews : News() {
    data object ShowErrorToast : RegistrationUserInfoNews()
    class ChangeImage(val uri: Uri?) : RegistrationUserInfoNews()
    data object OpenCodePage : RegistrationUserInfoNews()
}