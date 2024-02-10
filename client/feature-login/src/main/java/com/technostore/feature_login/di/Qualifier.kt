package com.technostore.feature_login.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignInStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegistrationStore