package com.technostore.feature_search.search.presentation

import com.technostore.arch.mvi.News

sealed class SearchNews : News() {
    data object ShowErrorToast : SearchNews()
    data object NavigateBack : SearchNews()
    data class OpenProductPage(val productId: Long) : SearchNews()
    data object OpenFilter : SearchNews()
}