package com.technostore.feature_profile.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProfileStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EditProfileStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChangePasswordStore