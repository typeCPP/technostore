package com.technostore.feature_login.di

import com.technostore.app_store.store.AppStore
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.business.LoginRepositoryImpl
import com.technostore.feature_login.registration.presentation.RegistrationEffectHandler
import com.technostore.feature_login.registration.presentation.RegistrationReducer
import com.technostore.feature_login.sign_in.presentation.SignInEffectHandler
import com.technostore.feature_login.sign_in.presentation.SignInReducer
import com.technostore.feature_login.welcome_page.presentation.WelcomePageEffectHandler
import com.technostore.feature_login.welcome_page.presentation.WelcomePageReducer
import com.technostore.network.service.LoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideLoginRepository(loginService: LoginService, appStore: AppStore): LoginRepository {
        return LoginRepositoryImpl(loginService, appStore)
    }

    /* Welcome page */
    @Provides
    fun provideWelcomePageEffectHandler(appStore: AppStore): WelcomePageEffectHandler {
        return WelcomePageEffectHandler(appStore)
    }

    @Provides
    fun provideWelcomePageReducer(
    ): WelcomePageReducer {
        return WelcomePageReducer()
    }

    /* Sign in */
    @Provides
    fun provideSignInEffectHandler(loginRepository: LoginRepository): SignInEffectHandler {
        return SignInEffectHandler(loginRepository)
    }

    @Provides
    fun provideSignInReducer(): SignInReducer {
        return SignInReducer()
    }

    /* Registration */
    @Provides
    fun provideRegistrationEffectHandler(loginRepository: LoginRepository): RegistrationEffectHandler {
        return RegistrationEffectHandler(loginRepository)
    }

    @Provides
    fun provideRegistrationReducer(): RegistrationReducer {
        return RegistrationReducer()
    }

}