package com.technostore.feature_profile.di

import com.technostore.app_store.store.AppStore
import com.technostore.arch.mvi.Store
import com.technostore.feature_profile.business.ProfileRepository
import com.technostore.feature_profile.business.ProfileRepositoryImpl
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEffectHandler
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEvent
import com.technostore.feature_profile.change_password.presentation.ChangePasswordReducer
import com.technostore.feature_profile.change_password.presentation.ChangePasswordState
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEffectHandler
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEvent
import com.technostore.feature_profile.edit_profile.presentation.EditProfileReducer
import com.technostore.feature_profile.edit_profile.presentation.EditProfileState
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileEvent
import com.technostore.feature_profile.profile.presentation.ProfileReducer
import com.technostore.feature_profile.profile.presentation.ProfileState
import com.technostore.network.service.SessionService
import com.technostore.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ProfileModule {

    @Provides
    fun provideProfileRepository(
        userService: UserService,
        sessionService: SessionService,
        appStore: AppStore
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            userService = userService,
            sessionService = sessionService,
            appStore = appStore
        )
    }

    /* Profile */
    @Provides
    fun provideProfileEffectHandler(profileRepository: ProfileRepository): ProfileEffectHandler {
        return ProfileEffectHandler(profileRepository)
    }

    @Provides
    fun provideProfilePageReducer(): ProfileReducer {
        return ProfileReducer()
    }

    @Provides
    fun provideProfileState(): ProfileState {
        return ProfileState()
    }

    @Provides
    fun provideProfileStore(
        effectHandler: ProfileEffectHandler,
        reducer: ProfileReducer,
        initialState: ProfileState
    ): Store<ProfileState, ProfileEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }

    /* Change password */
    @Provides
    fun provideChangePasswordEffectHandler(profileRepository: ProfileRepository): ChangePasswordEffectHandler {
        return ChangePasswordEffectHandler(profileRepository)
    }

    @Provides
    fun provideChangePasswordPageReducer(): ChangePasswordReducer {
        return ChangePasswordReducer()
    }

    @Provides
    fun provideChangePasswordState(): ChangePasswordState {
        return ChangePasswordState()
    }

    @Provides
    fun provideChangePasswordStore(
        initialState: ChangePasswordState,
        reducer: ChangePasswordReducer,
        effectHandler: ChangePasswordEffectHandler
    ): Store<ChangePasswordState, ChangePasswordEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }

    /* Edit profile */
    @Provides
    fun provideEditProfileEffectHandler(profileRepository: ProfileRepository): EditProfileEffectHandler {
        return EditProfileEffectHandler(profileRepository)
    }

    @Provides
    fun provideEditProfiledPageReducer(): EditProfileReducer {
        return EditProfileReducer()
    }

    @Provides
    fun provideEditProfileState(): EditProfileState {
        return EditProfileState()
    }

    @Provides
    fun provideEditProfileStore(
        initialState: EditProfileState,
        reducer: EditProfileReducer,
        effectHandler: EditProfileEffectHandler
    ): Store<EditProfileState, EditProfileEvent> {
        return Store(
            initialState = initialState,
            reducer = reducer,
            effectHandlers = listOf(effectHandler)
        )
    }
}