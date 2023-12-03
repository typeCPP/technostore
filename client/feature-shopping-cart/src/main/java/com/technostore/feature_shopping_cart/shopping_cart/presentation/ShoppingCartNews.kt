package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.mvi.News

sealed class ShoppingCartNews : News() {
    data object ShowErrorToast : ShoppingCartNews()
    data object ShowSuccessToast : ShoppingCartNews()
    data object OpenMainPage : ShoppingCartNews()
}