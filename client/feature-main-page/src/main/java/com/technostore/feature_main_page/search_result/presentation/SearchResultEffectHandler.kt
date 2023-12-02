package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_main_page.business.MainRepository
import com.technostore.shared_search.business.SharedSearchRepository

class SearchResultEffectHandler(
    private val mainRepository: MainRepository,
    private val sharedSearchRepository: SharedSearchRepository
) : EffectHandler<SearchResultState, SearchResultEvent> {
    override suspend fun process(
        event: SearchResultEvent,
        currentState: SearchResultState,
        store: Store<SearchResultState, SearchResultEvent>
    ) {
        when (event) {
            is SearchResultUiEvent.Init -> {

                store.dispatch(SearchResultEvent.StartLoading)
                if (event.isPopularity) {
                    val result = mainRepository.searchByPopularity()
                    when (result) {
                        is Result.Success -> {
                            store.dispatch(SearchResultEvent.EndLoading)
                            store.dispatch(SearchResultEvent.DataLoaded(result.data!!))
                        }

                        is Result.Error -> {
                            store.acceptNews(SearchResultNews.ShowErrorToast)

                        }
                    }
                } else {
                    val result = mainRepository.searchByCategory(event.categoryId!!)
                    when (result) {
                        is Result.Success -> {
                            store.dispatch(SearchResultEvent.EndLoading)
                            store.dispatch(SearchResultEvent.DataLoaded(result.data!!))
                        }

                        is Result.Error -> {
                            store.acceptNews(SearchResultNews.ShowErrorToast)

                        }
                    }
                }
            }

            SearchResultUiEvent.OnBackClicked -> {
                store.acceptNews(SearchResultNews.NavigateBack)
            }

            is SearchResultUiEvent.OnProductClicked -> {
                store.acceptNews(SearchResultNews.OpenProductPage(event.productId))
            }

            is SearchResultUiEvent.OnPlusClicked -> {
                val result = sharedSearchRepository.setProductCount(event.productId, event.count)
                if (result is Result.Error) {
                    store.acceptNews(SearchResultNews.ShowErrorToast)
                } else {
                    store.dispatch(SearchResultEvent.UpdateCount(event.productId, event.count))
                }
            }

            else -> {}
        }
    }
}