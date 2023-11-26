package com.technostore.feature_profile.edit_profile.presentation

import android.net.Uri
import com.technostore.arch.mvi.News

sealed class EditProfileNews : News() {
    data object ShowErrorToast : EditProfileNews()
    data object OpenPreviousPage : EditProfileNews()
    data object NameIsEmpty : EditProfileNews()
    data object LastNameIsEmpty : EditProfileNews()
    class ChangeImage(val uri: Uri?) : EditProfileNews()
}