package com.technostore.feature_profile.di

import com.technostore.app_store.store.AppStore
import com.technostore.feature_profile.business.ProfileRepository
import com.technostore.feature_profile.business.ProfileRepositoryImpl
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEffectHandler
import com.technostore.feature_profile.change_password.presentation.ChangePasswordReducer
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEffectHandler
import com.technostore.feature_profile.edit_profile.presentation.EditProfileReducer
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileReducer
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
        return ProfileRepositoryImpl(userService, sessionService, appStore)
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

    /* Change password */
    @Provides
    fun provideChangePasswordEffectHandler(profileRepository: ProfileRepository): ChangePasswordEffectHandler {
        return ChangePasswordEffectHandler(profileRepository)
    }

    @Provides
    fun provideChangePasswordPageReducer(): ChangePasswordReducer {
        return ChangePasswordReducer()
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
}