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
                            val reviewResult = productRepository.getUserReview(event.productId)
                            if (reviewResult is Result.Success) {
                                store.dispatch(ProductEvent.OnReviewLoaded(reviewResult.data!!.text))
                            }
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
                store.acceptNews(
                    ProductNews.OpenRateDialog(
                        reviewText = currentState.userReviewText,
                        userRating = currentState.productDetail?.userRating ?: 0
                    )
                )
            }

            is ProductUiEvent.OnBuyClicked -> {
                val newCount = (currentState.productDetail?.inCartCount ?: 0) + 1
                val result = productRepository.setProductCount(
                    event.productId,
                    newCount
                )
                if (result is Result.Error) {
                    store.acceptNews(ProductNews.ShowErrorToast)
                } else {
                    store.dispatch(ProductEvent.UpdateInCartCount(newCount))
                }
            }

            ProductUiEvent.OnMoreDescriptionClicked -> {
                store.acceptNews(
                    ProductNews.OpenDescription(
                        productName = currentState.productDetail?.name.orEmpty(),
                        description = currentState.productDetail?.description.orEmpty()
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
                if (currentState.productDetail == null) {
                    store.acceptNews(ProductNews.ShowErrorToast)
                    return
                }
                val result = productRepository.setReview(
                    currentState.productDetail.id,
                    event.rating,
                    event.text
                )
                when (result) {
                    is Result.Success -> {
                        val newReviews = productRepository.getReviews(currentState.productDetail.id)
                        if (newReviews is Result.Success && newReviews.data != null) {
                            store.dispatch(ProductEvent.UpdateReviews(newReviews.data!!))
                        }
                        store.dispatch(ProductEvent.UpdateRating(event.rating, event.text))
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