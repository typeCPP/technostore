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

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoginRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SessionRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ReviewRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProductRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OrderRetrofit