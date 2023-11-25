package com.technostore.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnregisteredOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnregisteredRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegisteredOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RegisteredRetrofit
