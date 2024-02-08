package com.technostore.shared_search.filter.presentation

import com.technostore.arch.mvi.News

sealed class FilterNews : News() {
    data object ShowErrorToast : FilterNews()
    data object OpenPreviousPage : FilterNews()
    data class ChangeIsSortingByPopularityBackground(val isSelected: Boolean) : FilterNews()
    data class ChangeIsSortingByRatingBackground(val isSelected: Boolean) : FilterNews()
}