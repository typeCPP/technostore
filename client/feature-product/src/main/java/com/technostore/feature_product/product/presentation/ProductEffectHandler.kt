package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.EffectHandler
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.feature_product.business.ProductRepository

class ProductEffectHandler(
    private val productRepository: ProductRepository
) : EffectHandler<ProductState, ProductEvent> {
    override suspend fun process(
        event: ProductEvent,
        currentState: ProductState,
        store: Store<ProductState, ProductEvent>
    ) {
        when (event) {
            is ProductUiEvent.Init -> {
                store.dispatch(ProductEvent.StartLoading)
                when (val result = productRepository.getProductById(event.productId)) {
                    is Result.Success -> {
                        if (result.data != null) {
                            store.dispatch(ProductEvent.StartLoading)
                            store.dispatch(ProductEvent.OnDataLoaded(result.data!!))
                        } else {
                            store.acceptNews(ProductNews.ShowErrorToast)
                        }
                    }

                    is Result.Error -> {
                        store.acceptNews(ProductNews.ShowErrorToast)
                    }
                }
            }

            ProductUiEvent.OnRateClicked -> {
                store.acceptNews(ProductNews.OpenRateDialog)
            }

            is ProductUiEvent.OnBuyClicked -> {
                TODO()
            }

            ProductUiEvent.OnMoreDescriptionClicked -> {
                store.acceptNews(ProductNews.OpenDescription)
            }

            is ProductUiEvent.OnMoreReviewClicked -> {
                store.acceptNews(ProductNews.OpenReviewsListPage(event.productId))
            }

            else -> {}
        }
    }
}