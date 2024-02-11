package com.technostore.feature_login.di

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.Store
import com.technostore.feature_login.business.LoginRepository
import com.technostore.feature_login.business.LoginRepositoryImpl
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeEffectHandler
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeReducer
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailEffectHandler
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailReducer
import com.technostore.feature_login.registration.presentation.RegistrationEffectHandler
import com.technostore.feature_login.registration.presentation.RegistrationReducer
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoEffectHandler
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoReducer
import com.technostore.feature_login.sign_in.presentation.SignInEffectHandler
import com.technostore.feature_login.sign_in.presentation.SignInReducer
import com.technostore.feature_login.welcome_page.presentation.WelcomePageEffectHandler
import com.technostore.feature_login.welcome_page.presentation.WelcomePageReducer
import com.technostore.network.service.LoginService
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryEffectHandler
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryEvent
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryReducer
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryState
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeEffectHandler
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeEvent
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeReducer
import com.technostore.feature_login.registration.presentation.RegistrationEvent
import com.technostore.feature_login.registration.presentation.RegistrationState
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeState
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailEvent
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailState
import com.technostore.feature_login.sign_in.presentation.SignInEvent
import com.technostore.feature_login.sign_in.presentation.SignInState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Provides
    fun provideLoginRepository(
        loginService: LoginService,
        sessionService: SessionService,
        userService: UserService,
        appStore: AppStore
    ): LoginRepository {
        return LoginRepositoryImpl(
            loginService,
            appStore,
            sessionService,
            userService
        )
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

    @Provides
    fun provideSignInState(): SignInState {
        return SignInState()
    }

    @Provides
    fun provideSignInStore(
        signInEffectHandler: SignInEffectHandler,
        signInReducer: SignInReducer,
        initialState: SignInState
    ): Store<SignInState, SignInEvent> {
        return Store(
            initialState = initialState,
            reducer = signInReducer,
            effectHandlers = listOf(signInEffectHandler)
        )
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

    @Provides
    fun provideRegistrationState(): RegistrationState {
        return RegistrationState()
    }

    @Provides
    fun provideRegistrationStore(
        effectHandler: RegistrationEffectHandler,
        reducer: RegistrationReducer,
        initialState: RegistrationState
    ): Store<RegistrationState, RegistrationEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }


    /* Registration User Info */
    @Provides
    fun provideRegistrationUserInfoEffectHandler(loginRepository: LoginRepository): RegistrationUserInfoEffectHandler {
        return RegistrationUserInfoEffectHandler(loginRepository)
    }

    @Provides
    fun provideRegistrationUserInfoReducer(): RegistrationUserInfoReducer {
        return RegistrationUserInfoReducer()
    }

    /* Confirmation code */
    @Provides
    fun provideConfirmationCodeEffectHandler(loginRepository: LoginRepository): ConfirmationCodeEffectHandler {
        return ConfirmationCodeEffectHandler(loginRepository)
    }

    @Provides
    fun provideConfirmationCodeReducer(): ConfirmationCodeReducer {
        return ConfirmationCodeReducer()
    }

    /* Password recovery email */
    @Provides
    fun providePasswordRecoveryEmailEffectHandler(loginRepository: LoginRepository): PasswordRecoveryEmailEffectHandler {
        return PasswordRecoveryEmailEffectHandler(loginRepository)
    }

    @Provides
    fun providePasswordRecoveryEmailReducer(): PasswordRecoveryEmailReducer {
        return PasswordRecoveryEmailReducer()
    }

    @Provides
    fun providePasswordRecoveryEmailState(): PasswordRecoveryEmailState {
        return PasswordRecoveryEmailState()
    }

    @Provides
    fun providePasswordRecoveryEmailStore(
        initialState: PasswordRecoveryEmailState,
        reducer: PasswordRecoveryEmailReducer,
        effectHandler: PasswordRecoveryEmailEffectHandler
    ): Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }

    /* Password recovery code */
    @Provides
    fun providePasswordRecoveryCodeEffectHandler(loginRepository: LoginRepository): PasswordRecoveryCodeEffectHandler {
        return PasswordRecoveryCodeEffectHandler(loginRepository)
    }

    @Provides
    fun providePasswordRecoveryCodeReducer(): PasswordRecoveryCodeReducer {
        return PasswordRecoveryCodeReducer()
    }

    @Provides
    fun providePasswordRecoveryCodeState(): PasswordRecoveryCodeState {
        return PasswordRecoveryCodeState()
    }

    @Provides
    fun providePasswordRecoveryCodeStore(
        initialState: PasswordRecoveryCodeState,
        reducer: PasswordRecoveryCodeReducer,
        effectHandler: PasswordRecoveryCodeEffectHandler
    ): Store<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }

    /* Password recovery */
    @Provides
    fun providePasswordRecoveryEffectHandler(loginRepository: LoginRepository): PasswordRecoveryEffectHandler {
        return PasswordRecoveryEffectHandler(loginRepository)
    }

    @Provides
    fun providePasswordRecoveryReducer(): PasswordRecoveryReducer {
        return PasswordRecoveryReducer()
    }

    @Provides
    fun providePasswordRecoveryState(): PasswordRecoveryState {
        return PasswordRecoveryState()
    }

    @Provides
    fun providePasswordRecoveryStore(
        initialState: PasswordRecoveryState,
        reducer: PasswordRecoveryReducer,
        effectHandler: PasswordRecoveryEffectHandler
    ): Store<PasswordRecoveryState, PasswordRecoveryEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }
}