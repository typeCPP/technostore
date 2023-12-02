package com.technostore.feature_main_page.search_result.presentation

import com.technostore.arch.mvi.News

sealed class SearchResultNews : News() {
    data object ShowErrorToast : SearchResultNews()
    data object NavigateBack : SearchResultNews()
    data class OpenProductPage(val productId: Long) : SearchResultNews()
}