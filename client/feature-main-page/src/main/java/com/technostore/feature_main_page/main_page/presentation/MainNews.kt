package com.technostore.feature_main_page.main_page.presentation

import com.technostore.arch.mvi.News

sealed class MainNews : News() {
    data object ShowErrorToast : MainNews()
    data object NavigateBack : MainNews()
    data class OpenProductPage(val productId: Long) : MainNews()
    data object OpenFilter : MainNews()
    data class OpenResultByCategory(val categoryId: Long) : MainNews()
    data object OpenResultByPopularity : MainNews()
}