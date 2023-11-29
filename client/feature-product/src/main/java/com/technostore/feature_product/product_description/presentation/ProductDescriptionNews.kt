package com.technostore.feature_product.product_description.presentation

import com.technostore.arch.mvi.News

sealed class ProductDescriptionNews : News() {
    data object OpenPreviousPage : ProductDescriptionNews()
}
