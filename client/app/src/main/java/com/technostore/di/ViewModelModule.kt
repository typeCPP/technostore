package com.technostore.di

import com.technostore.arch.mvi.InitialState
import com.technostore.base.presentation.BaseEffectHandler
import com.technostore.base.presentation.BaseReducer
import com.technostore.base.presentation.BaseViewModel
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeEffectHandler
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeReducer
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeState
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeViewModel
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailEffectHandler
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailReducer
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailState
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailViewModel
import com.technostore.feature_login.registration.presentation.RegistrationEffectHandler
import com.technostore.feature_login.registration.presentation.RegistrationReducer
import com.technostore.feature_login.registration.presentation.RegistrationState
import com.technostore.feature_login.registration.presentation.RegistrationViewModel
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoEffectHandler
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoReducer
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoState
import com.technostore.feature_login.registration_user_info.presentation.RegistrationUserInfoViewModel
import com.technostore.feature_login.sign_in.presentation.SignInEffectHandler
import com.technostore.feature_login.sign_in.presentation.SignInReducer
import com.technostore.feature_login.sign_in.presentation.SignInState
import com.technostore.feature_login.sign_in.presentation.SignInViewModel
import com.technostore.feature_login.welcome_page.presentation.WelcomePageEffectHandler
import com.technostore.feature_login.welcome_page.presentation.WelcomePageReducer
import com.technostore.feature_login.welcome_page.presentation.WelcomePageViewModel
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryEffectHandler
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryReducer
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryState
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryViewModel
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeEffectHandler
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeReducer
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeState
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeViewModel
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileReducer
import com.technostore.feature_profile.profile.presentation.ProfileState
import com.technostore.feature_profile.profile.presentation.ProfileViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ViewModelModule {

    @Provides
    fun provideBaseViewModel(
        reducer: BaseReducer,
        effectHandler: BaseEffectHandler
    ): BaseViewModel {
        return BaseViewModel(
            initialState = InitialState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideWelcomePageViewModel(
        reducer: WelcomePageReducer,
        effectHandler: WelcomePageEffectHandler
    ): WelcomePageViewModel {
        return WelcomePageViewModel(
            initialState = InitialState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideWelcomeSignInViewModel(
        reducer: SignInReducer,
        effectHandler: SignInEffectHandler
    ): SignInViewModel {
        return SignInViewModel(
            initialState = SignInState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideRegistrationViewModel(
        reducer: RegistrationReducer,
        effectHandler: RegistrationEffectHandler
    ): RegistrationViewModel {
        return RegistrationViewModel(
            initialState = RegistrationState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideRegistrationUserInfoViewModel(
        reducer: RegistrationUserInfoReducer,
        effectHandler: RegistrationUserInfoEffectHandler
    ): RegistrationUserInfoViewModel {
        return RegistrationUserInfoViewModel(
            initialState = RegistrationUserInfoState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideConfirmationCodeViewModel(
        reducer: ConfirmationCodeReducer,
        effectHandler: ConfirmationCodeEffectHandler
    ): ConfirmationCodeViewModel {
        return ConfirmationCodeViewModel(
            initialState = ConfirmationCodeState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun providePasswordRecoveryEmailViewModel(
        reducer: PasswordRecoveryEmailReducer,
        effectHandler: PasswordRecoveryEmailEffectHandler
    ): PasswordRecoveryEmailViewModel {
        return PasswordRecoveryEmailViewModel(
            initialState = PasswordRecoveryEmailState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }


    @Provides
    fun providePasswordRecoveryCodeViewModel(
        reducer: PasswordRecoveryCodeReducer,
        effectHandler: PasswordRecoveryCodeEffectHandler
    ): PasswordRecoveryCodeViewModel {
        return PasswordRecoveryCodeViewModel(
            initialState = PasswordRecoveryCodeState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun providePasswordRecoveryViewModel(
        reducer: PasswordRecoveryReducer,
        effectHandler: PasswordRecoveryEffectHandler
    ): PasswordRecoveryViewModel {
        return PasswordRecoveryViewModel(
            initialState = PasswordRecoveryState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideProfileViewModel(
        reducer: ProfileReducer,
        effectHandler: ProfileEffectHandler
    ): ProfileViewModel {
        return ProfileViewModel(
            initialState = ProfileState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }
}