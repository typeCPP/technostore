package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.News

sealed class ProductNews : News() {
    data object ShowErrorToast : ProductNews()
    data class OpenRateDialog(val reviewText: String?, val userRating: Int) : ProductNews()
    data class OpenDescription(
        val productName: String,
        val description: String
    ) : ProductNews()

    data class OpenReviewsListPage(val productId: Long) : ProductNews()
    data object OpenPreviousPage : ProductNews()
}