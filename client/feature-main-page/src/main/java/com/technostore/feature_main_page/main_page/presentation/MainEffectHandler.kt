package com.technostore.feature_main_page.main_page.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_main_page.business.MainRepository
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.error.SearchEmpty

class MainEffectHandler(
    private val sharedSearchRepository: SharedSearchRepository,
    private val mainRepository: MainRepository
) : EffectHandler<MainState, MainEvent> {
    override suspend fun process(
        event: MainEvent,
        currentState: MainState,
        store: Store<MainState, MainEvent>
    ) {
        when (event) {
            is MainUiEvent.Init -> {
                sharedSearchRepository.clearNumberPage()
                store.dispatch(MainEvent.StartLoading)
                val result = mainRepository.searchByPopularity()
                if (result is Result.Success) {
                    val data = result.data
                    if (data != null) {
                        val categoriesResult = sharedSearchRepository.getCategories()
                        if (categoriesResult is Result.Success) {
                            val categoriesData = categoriesResult.data
                            if (categoriesData != null) {
                                store.dispatch(MainEvent.EndLoading)
                                store.dispatch(
                                    MainEvent.MainDataLoaded(
                                        popularProducts = data,
                                        categories = categoriesData
                                    )
                                )
                                return
                            }
                        }
                    }
                }
                store.acceptNews(MainNews.ShowErrorToast)
            }

            is MainUiEvent.OnTextChanged -> {
                store.dispatch(MainEvent.StartLoading)
                val result = sharedSearchRepository.searchProducts(event.text)
                when (result) {
                    is Result.Success -> {
                        val data = result.data
                        if (data != null) {
                            store.dispatch(MainEvent.EndLoading)
                            store.dispatch(MainEvent.DataLoaded(data))
                        } else {
                            store.acceptNews(MainNews.ShowErrorToast)
                        }
                    }

                    is Result.Error -> {
                        if (result.error != null && result.error is SearchEmpty) {
                            store.dispatch(MainEvent.IsEmpty)
                        } else {
                            store.acceptNews(MainNews.ShowErrorToast)
                        }
                    }
                }
            }

            is MainUiEvent.LoadMoreProducts -> {
                val result = sharedSearchRepository.searchProducts(event.text)
                when (result) {
                    is Result.Success -> {
                        val data = result.data
                        if (data != null) {
                            store.dispatch(MainEvent.DataLoaded(data))
                        } else {
                            store.acceptNews(MainNews.ShowErrorToast)
                        }
                    }

                    is Result.Error -> {
                        if (!(result.error != null && result.error is SearchEmpty)) {
                            store.acceptNews(MainNews.ShowErrorToast)
                        }
                    }
                }
            }

            MainUiEvent.OnBackClicked -> {
                store.acceptNews(MainNews.NavigateBack)
                store.dispatch(MainEvent.ClearData)
            }

            is MainUiEvent.OnProductClicked -> {
                store.acceptNews(MainNews.OpenProductPage(event.productId))
            }

            is MainUiEvent.OnPlusClicked -> {
                val result = sharedSearchRepository.setProductCount(event.productId, event.count)
                if (result is Result.Error) {
                    store.acceptNews(MainNews.ShowErrorToast)
                } else {
                    store.dispatch(MainEvent.UpdateCount(event.productId, event.count))
                }
            }

            MainUiEvent.OnFilterClicked -> {
                store.acceptNews(MainNews.OpenFilter)
            }

            is MainUiEvent.OnCategoryClicked -> {
                store.acceptNews(MainNews.OpenResultByCategory(event.categoryId))
            }

            MainUiEvent.OnSearchCLicked -> {
                store.dispatch(MainEvent.SetIsMainPage(false))
            }

            MainUiEvent.OnTextIsEmpty -> {
                store.dispatch(MainEvent.SetIsMainPage(true))
            }

            MainUiEvent.MorePopularClicked -> {
                store.acceptNews(MainNews.OpenResultByPopularity)
            }

            else -> {}
        }
    }
}