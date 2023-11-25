package com.technostore.feature_profile.di

import com.technostore.feature_profile.business.ProfileRepository
import com.technostore.feature_profile.business.ProfileRepositoryImpl
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileReducer
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
        userService: UserService
    ): ProfileRepository {
        return ProfileRepositoryImpl(userService)
    }

    /* Profile */
    @Provides
    fun provideProfileEffectHandler(profileRepository: ProfileRepository): ProfileEffectHandler {
        return ProfileEffectHandler(profileRepository)
    }

    @Provides
    fun provideWelcomePageReducer(): ProfileReducer {
        return ProfileReducer()
    }
}