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
import com.technostore.feature_login.di.RegistrationStore
import com.technostore.feature_login.di.PasswordRecoveryCodeStore
import com.technostore.feature_login.di.PasswordRecoveryEmailStore
import com.technostore.feature_login.di.PasswordRecoveryStore
import com.technostore.feature_login.di.SignInStore
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailState
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailViewModel
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
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryEvent
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryState
import com.technostore.feature_login.password_recovery.presentation.PasswordRecoveryViewModel
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeEvent
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeState
import com.technostore.feature_login.password_recovery_code.presentation.PasswordRecoveryCodeViewModel
import com.technostore.feature_login.password_recovery_email.presentation.PasswordRecoveryEmailEvent
import com.technostore.feature_login.registration.presentation.RegistrationEvent
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
import com.technostore.feature_profile.change_password.presentation.ChangePasswordEvent
import com.technostore.feature_profile.change_password.presentation.ChangePasswordState
import com.technostore.feature_profile.change_password.presentation.ChangePasswordViewModel
import com.technostore.feature_profile.di.ProfileStore
import com.technostore.feature_profile.di.ChangePasswordStore
import com.technostore.feature_profile.di.EditProfileStore
import com.technostore.feature_profile.edit_profile.presentation.EditProfileEvent
import com.technostore.feature_profile.edit_profile.presentation.EditProfileState
import com.technostore.feature_profile.edit_profile.presentation.EditProfileViewModel
import com.technostore.feature_profile.profile.presentation.ProfileEvent
import com.technostore.feature_profile.profile.presentation.ProfileState
import com.technostore.feature_profile.profile.presentation.ProfileViewModel
import com.technostore.feature_search.di.SearchStore
import com.technostore.feature_search.search.presentation.SearchEvent
import com.technostore.feature_search.search.presentation.SearchState
import com.technostore.feature_search.search.presentation.SearchViewModel
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartEffectHandler
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartReducer
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartState
import com.technostore.feature_shopping_cart.shopping_cart.presentation.ShoppingCartViewModel
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
        @RegistrationStore store: Store<RegistrationState, RegistrationEvent>
    ): RegistrationViewModel {
        return RegistrationViewModel(store)
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
        @PasswordRecoveryEmailStore store: Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent>
    ): PasswordRecoveryEmailViewModel {
        return PasswordRecoveryEmailViewModel(store)
    }


    @Provides
    fun providePasswordRecoveryCodeViewModel(
        @PasswordRecoveryCodeStore store: Store<PasswordRecoveryCodeState, PasswordRecoveryCodeEvent>
    ): PasswordRecoveryCodeViewModel {
        return PasswordRecoveryCodeViewModel(store)
    }

    @Provides
    fun providePasswordRecoveryViewModel(
        @PasswordRecoveryStore store: Store<PasswordRecoveryState, PasswordRecoveryEvent>
    ): PasswordRecoveryViewModel {
        return PasswordRecoveryViewModel(store)
    }

    @Provides
    fun provideProfileViewModel(
        @ProfileStore store: Store<ProfileState, ProfileEvent>
    ): ProfileViewModel {
        return ProfileViewModel(store)
    }

    @Provides
    fun provideChangePasswordViewModel(
        @ChangePasswordStore store: Store<ChangePasswordState, ChangePasswordEvent>
    ): ChangePasswordViewModel {
        return ChangePasswordViewModel(store)
    }

    @Provides
    fun provideEditProfileViewModel(
        @EditProfileStore store: Store<EditProfileState, EditProfileEvent>
    ): EditProfileViewModel {
        return EditProfileViewModel(store)
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
    fun provideSearchViewModel(
        @SearchStore store: Store<SearchState, SearchEvent>
    ): SearchViewModel {
        return SearchViewModel(store)
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
