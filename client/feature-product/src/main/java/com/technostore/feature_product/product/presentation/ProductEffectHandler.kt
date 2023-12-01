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
                val result = productRepository.getProductById(event.productId)
                when (result) {
                    is Result.Success -> {
                        if (result.data != null) {
                            store.dispatch(ProductEvent.EndLoading)
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
                store.acceptNews(
                    ProductNews.OpenDescription(
                        productName = currentState.productDetail?.name!!,
                        description = currentState.productDetail.description
                    )
                )
            }

            is ProductUiEvent.OnMoreReviewClicked -> {
                store.acceptNews(ProductNews.OpenReviewsListPage(event.productId))
            }

            is ProductUiEvent.OnBackClicked -> {
                store.acceptNews(ProductNews.OpenPreviousPage)
            }

            is ProductUiEvent.SetReview -> {
                val result = productRepository.setReview(
                    currentState.productDetail?.id!!,
                    event.rating,
                    event.text
                )
                when (result) {
                    is Result.Success -> {
                        if (result.data != null) {
                            store.dispatch(ProductEvent.UpdateRating(event.rating))
                        } else {
                            store.acceptNews(ProductNews.ShowErrorToast)
                        }
                    }
                    is Result.Error -> {
                        store.acceptNews(ProductNews.ShowErrorToast)
                    }
                }
            }

            else -> {}
        }
    }
}