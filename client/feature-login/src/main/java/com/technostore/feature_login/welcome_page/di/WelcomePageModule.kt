package com.technostore.feature_login.welcome_page.di

import com.technostore.feature_login.welcome_page.presentation.WelcomePageEffectHandler
import com.technostore.feature_login.welcome_page.presentation.WelcomePageReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class WelcomePageModule {

    @Provides
    fun provideWelcomePageEffectHandler(): WelcomePageEffectHandler {
        return WelcomePageEffectHandler()
    }

    @Provides
    fun provideWelcomePageReducer(
    ): WelcomePageReducer {
        return WelcomePageReducer()
    }
}