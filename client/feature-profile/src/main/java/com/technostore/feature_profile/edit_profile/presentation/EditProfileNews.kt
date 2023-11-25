package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.News

sealed class EditProfileNews : News() {
    data object ShowErrorToast : EditProfileNews()
    data object OpenPreviousPage : EditProfileNews()
    data object NameIsEmpty : EditProfileNews()
    data object LastNameIsEmpty : EditProfileNews()
}