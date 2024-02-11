package com.technostore.di

import com.technostore.arch.mvi.InitialState
import com.technostore.base.presentation.BaseEffectHandler
import com.technostore.base.presentation.BaseReducer
import com.technostore.base.presentation.BaseViewModel
import com.technostore.feature_login.welcome_page.presentation.WelcomePageEffectHandler
import com.technostore.feature_login.welcome_page.presentation.WelcomePageReducer
import com.technostore.feature_login.welcome_page.presentation.WelcomePageViewModel
import com.technostore.feature_main_page.main_page.presentation.MainEffectHandler
import com.technostore.feature_main_page.main_page.presentation.MainReducer
import com.technostore.feature_main_page.main_page.presentation.MainState
import com.technostore.feature_main_page.main_page.presentation.MainViewModel
import com.technostore.feature_main_page.search_result.presentation.SearchResultEffectHandler
import com.technostore.feature_main_page.search_result.presentation.SearchResultReducer
import com.technostore.feature_main_page.search_result.presentation.SearchResultState
import com.technostore.feature_main_page.search_result.presentation.SearchResultViewModel
import com.technostore.feature_product.product_description.presentation.ProductDescriptionEffectHandler
import com.technostore.feature_product.product_description.presentation.ProductDescriptionReducer
import com.technostore.feature_product.product_description.presentation.ProductDescriptionViewModel
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
