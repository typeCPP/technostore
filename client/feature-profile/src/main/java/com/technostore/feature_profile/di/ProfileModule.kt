package com.technostore.feature_profile.di

import com.technostore.app_store.store.AppStore
import com.technostore.feature_profile.business.ProfileRepository
import com.technostore.feature_profile.business.ProfileRepositoryImpl
import com.technostore.feature_profile.business.model.mapper.OrderDetailMapper
import com.technostore.feature_profile.business.model.mapper.ProductOrderMapper
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEffectHandler
import com.technostore.feature_profile.change_password.presentation.ChangePasswordReducer
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEffectHandler
import com.technostore.feature_profile.edit_profile.presentation.EditProfileReducer
import com.technostore.feature_profile.order_detail.presentation.OrderDetailEffectHandler
import com.technostore.feature_profile.order_detail.presentation.OrderDetailReducer
import com.technostore.feature_profile.orders.presentation.OrdersEffectHandler
import com.technostore.feature_profile.orders.presentation.OrdersReducer
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileReducer
import com.technostore.network.service.OrderService
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
    fun provideProductOrderMapper(): ProductOrderMapper {
        return ProductOrderMapper()
    }

    @Provides
    fun provideOrderDetailMapper(productOrderMapper: ProductOrderMapper): OrderDetailMapper {
        return OrderDetailMapper(productOrderMapper)
    }

    @Provides
    fun provideProfileRepository(
        userService: UserService,
        sessionService: SessionService,
        orderService: OrderService,
        orderDetailMapper: OrderDetailMapper,
        appStore: AppStore
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            userService = userService,
            sessionService = sessionService,
            orderService = orderService,
            orderDetailMapper = orderDetailMapper,
            appStore = appStore
        )
    }

    /* Profile */
    @Provides
    fun provideProfileEffectHandler(profileRepository: ProfileRepository): ProfileEffectHandler {
        return ProfileEffectHandler(profileRepository)
    }

    @Provides
    fun provideProfilePageReducer(appStore: AppStore): ProfileReducer {
        return ProfileReducer(appStore)
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

    /* Orders list */
    @Provides
    fun provideOrdersEffectHandler(profileRepository: ProfileRepository): OrdersEffectHandler {
        return OrdersEffectHandler(profileRepository)
    }

    @Provides
    fun provideOrdersReducer(): OrdersReducer {
        return OrdersReducer()
    }

    /* Order detail */

    @Provides
    fun provideOrderDetailEffectHandler(profileRepository: ProfileRepository): OrderDetailEffectHandler {
        return OrderDetailEffectHandler(profileRepository)
    }

    @Provides
    fun provideOrderDetailReducer(): OrderDetailReducer {
        return OrderDetailReducer()
    }
}