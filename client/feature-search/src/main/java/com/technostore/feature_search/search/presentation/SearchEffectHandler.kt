package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.shared_search.business.SharedSearchRepository
import com.technostore.shared_search.business.error.SearchEmpty

class SearchEffectHandler(
    private val sharedSearchRepository: SharedSearchRepository
) : EffectHandler<SearchState, SearchEvent> {
    override suspend fun process(
        event: SearchEvent,
        currentState: SearchState,
        store: Store<SearchState, SearchEvent>
    ) {
        when (event) {
            SearchUiEvent.Init -> {
                sharedSearchRepository.clearNumberPage()
            }

            is SearchUiEvent.OnTextChanged -> {
                store.dispatch(SearchEvent.StartLoading)
                val result = sharedSearchRepository.searchProducts(event.text)
                when (result) {
                    is Result.Success -> {
                        store.dispatch(SearchEvent.EndLoading)
                        store.dispatch(SearchEvent.DataLoaded(result.data!!))
                    }

                    is Result.Error -> {
                        if (result.error != null) {
                            if (result.error is SearchEmpty) {
                                store.dispatch(SearchEvent.IsEmpty)
                            }
                        } else {
                            store.acceptNews(SearchNews.ShowErrorToast)
                        }
                    }
                }
            }

            is SearchUiEvent.LoadMoreProducts -> {
                val result = sharedSearchRepository.searchProducts(event.text)
                when (result) {
                    is Result.Success -> {
                        store.dispatch(SearchEvent.DataLoaded(result.data!!))
                    }

                    is Result.Error -> {
                        if (!(result.error != null && result.error is SearchEmpty)) {
                            store.acceptNews(SearchNews.ShowErrorToast)
                        }
                    }
                }
            }

            SearchUiEvent.OnBackClicked -> {
                store.acceptNews(SearchNews.NavigateBack)
                store.dispatch(SearchEvent.ClearData)
            }

            is SearchUiEvent.OnProductClicked -> {
                store.acceptNews(SearchNews.OpenProductPage(event.productId))
            }

            is SearchUiEvent.OnPlusClicked -> {
                val result = sharedSearchRepository.setProductCount(event.productId, event.count)
                if (result is Result.Error) {
                    store.acceptNews(SearchNews.ShowErrorToast)
                } else {
                    store.dispatch(SearchEvent.UpdateCount(event.productId, event.count))
                }
            }

            SearchUiEvent.OnFilterClicked -> {
                store.acceptNews(SearchNews.OpenFilter)
            }

            else -> {}
        }
    }
}