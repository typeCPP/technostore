package com.technostore.di

import com.technostore.arch.mvi.InitialState
import com.technostore.arch.mvi.Store
import com.technostore.base.presentation.BaseEffectHandler
import com.technostore.base.presentation.BaseReducer
import com.technostore.base.presentation.BaseViewModel
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeEffectHandler
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeReducer
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeState
import com.technostore.feature_login.confirm_code.presentation.ConfirmationCodeViewModel
import com.technostore.feature_login.di.SignInStore
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
import com.technostore.feature_login.sign_in.presentation.SignInEvent
import com.technostore.feature_main_page.main_page.presentation.MainEffectHandler
import com.technostore.feature_main_page.main_page.presentation.MainReducer
import com.technostore.feature_main_page.main_page.presentation.MainState
import com.technostore.feature_main_page.main_page.presentation.MainViewModel
import com.technostore.feature_main_page.search_result.presentation.SearchResultEffectHandler
import com.technostore.feature_main_page.search_result.presentation.SearchResultReducer
import com.technostore.feature_main_page.search_result.presentation.SearchResultState
import com.technostore.feature_main_page.search_result.presentation.SearchResultViewModel
import com.technostore.feature_order.di.OrderDetailStore
import com.technostore.feature_order.di.OrdersStore
import com.technostore.feature_order.order_detail.presentation.OrderDetailEvent
import com.technostore.feature_order.order_detail.presentation.OrderDetailState
import com.technostore.feature_order.order_detail.presentation.OrderDetailViewModel
import com.technostore.feature_order.orders.presentation.OrdersEvent
import com.technostore.feature_order.orders.presentation.OrdersState
import com.technostore.feature_order.orders.presentation.OrdersViewModel
import com.technostore.feature_product.product.presentation.ProductEffectHandler
import com.technostore.feature_product.product.presentation.ProductReducer
import com.technostore.feature_product.product.presentation.ProductState
import com.technostore.feature_product.product.presentation.ProductViewModel
import com.technostore.feature_product.product_description.presentation.ProductDescriptionEffectHandler
import com.technostore.feature_product.product_description.presentation.ProductDescriptionReducer
import com.technostore.feature_product.product_description.presentation.ProductDescriptionViewModel
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEffectHandler
import com.technostore.feature_profile.change_password.presentation.ChangePasswordReducer
import com.technostore.feature_profile.change_password.presentation.ChangePasswordState
import com.technostore.feature_profile.change_password.presentation.ChangePasswordViewModel
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEffectHandler
import com.technostore.feature_profile.edit_profile.presentation.EditProfileReducer
import com.technostore.feature_profile.edit_profile.presentation.EditProfileState
import com.technostore.feature_profile.edit_profile.presentation.EditProfileViewModel
import com.technostore.feature_profile.profile.presentation.ProfileEffectHandler
import com.technostore.feature_profile.profile.presentation.ProfileReducer
import com.technostore.feature_profile.profile.presentation.ProfileState
import com.technostore.feature_profile.profile.presentation.ProfileViewModel
import com.technostore.feature_search.search.presentation.SearchEffectHandler
import com.technostore.feature_search.search.presentation.SearchReducer
import com.technostore.feature_search.search.presentation.SearchState
import com.technostore.feature_search.search.presentation.SearchViewModel
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartEffectHandler
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartReducer
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartState
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartViewModel
import com.technostore.review.presentation.ReviewEffectHandler
import com.technostore.review.presentation.ReviewReducer
import com.technostore.review.presentation.ReviewState
import com.technostore.review.presentation.ReviewViewModel
import com.technostore.review_list.presentation.ReviewListEffectHandler
import com.technostore.review_list.presentation.ReviewListReducer
import com.technostore.review_list.presentation.ReviewListState
import com.technostore.review_list.presentation.ReviewListViewModel
import com.technostore.shared_search.filter.presentation.FilterEffectHandler
import com.technostore.shared_search.filter.presentation.FilterReducer
import com.technostore.shared_search.filter.presentation.FilterState
import com.technostore.shared_search.filter.presentation.FilterViewModel
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
        @SignInStore store: Store<SignInState, SignInEvent>
    ): SignInViewModel {
        return SignInViewModel(store)
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

    @Provides
    fun provideChangePasswordViewModel(
        reducer: ChangePasswordReducer,
        effectHandler: ChangePasswordEffectHandler
    ): ChangePasswordViewModel {
        return ChangePasswordViewModel(
            initialState = ChangePasswordState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideEditProfileViewModel(
        reducer: EditProfileReducer,
        effectHandler: EditProfileEffectHandler
    ): EditProfileViewModel {
        return EditProfileViewModel(
            initialState = EditProfileState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideFilterViewModel(
        reducer: FilterReducer,
        effectHandler: FilterEffectHandler
    ): FilterViewModel {
        return FilterViewModel(
            initialState = FilterState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideProductViewModel(
        reducer: ProductReducer,
        effectHandler: ProductEffectHandler
    ): ProductViewModel {
        return ProductViewModel(
            initialState = ProductState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideProductDescriptionViewModel(
        reducer: ProductDescriptionReducer,
        effectHandler: ProductDescriptionEffectHandler
    ): ProductDescriptionViewModel {
        return ProductDescriptionViewModel(
            initialState = InitialState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideOrdersViewModel(
        @OrdersStore store: Store<OrdersState, OrdersEvent>
    ): OrdersViewModel {
        return OrdersViewModel(store)
    }

    @Provides
    fun provideOrderDetailViewModel(
        @OrderDetailStore store: Store<OrderDetailState, OrderDetailEvent>
    ): OrderDetailViewModel {
        return OrderDetailViewModel(store)
    }

    @Provides
    fun provideShoppingCartViewModel(
        reducer: ShoppingCartReducer,
        effectHandler: ShoppingCartEffectHandler
    ): ShoppingCartViewModel {
        return ShoppingCartViewModel(
            initialState = ShoppingCartState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideReviewListViewModel(
        reducer: ReviewListReducer,
        effectHandler: ReviewListEffectHandler
    ): ReviewListViewModel {
        return ReviewListViewModel(
            initialState = ReviewListState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideReviewViewModel(
        reducer: ReviewReducer,
        effectHandler: ReviewEffectHandler
    ): ReviewViewModel {
        return ReviewViewModel(
            initialState = ReviewState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideSearchViewModel(
        reducer: SearchReducer,
        effectHandler: SearchEffectHandler
    ): SearchViewModel {
        return SearchViewModel(
            initialState = SearchState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideMainViewModel(
        reducer: MainReducer,
        effectHandler: MainEffectHandler
    ): MainViewModel {
        return MainViewModel(
            initialState = MainState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }

    @Provides
    fun provideSearchResultViewModel(
        reducer: SearchResultReducer,
        effectHandler: SearchResultEffectHandler
    ): SearchResultViewModel {
        return SearchResultViewModel(
            initialState = SearchResultState(),
            reducer = reducer,
            effectHandler = effectHandler
        )
    }
}
