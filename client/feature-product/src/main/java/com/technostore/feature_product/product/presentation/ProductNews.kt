package com.technostore.feature_product.product.presentation

import com.technostore.arch.mvi.News

sealed class ProductNews : News() {
    data object ShowErrorToast : ProductNews()
    data object OpenRateDialog : ProductNews()
    data object OpenDescription : ProductNews()
    class OpenReviewsListPage(val productId: Long) : ProductNews()
}