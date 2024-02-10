package com.technostore.feature_login.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignInStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PasswordRecoveryStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PasswordRecoveryEmailStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PasswordRecoveryCodeStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegistrationStore