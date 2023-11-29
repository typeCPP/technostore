package com.technostore.feature_profile.order_detail.presentation

import com.technostore.arch.mvi.News

sealed class OrderDetailNews : News() {
    data object ShowErrorToast : OrderDetailNews()
    data object OpenPreviousPage : OrderDetailNews()
    class OpenProductPage(val id: Long) : OrderDetailNews()
}