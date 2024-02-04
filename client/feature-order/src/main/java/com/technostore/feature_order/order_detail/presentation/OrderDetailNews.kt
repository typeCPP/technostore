package com.technostore.feature_order.order_detail.presentation

import com.technostore.arch.mvi.News

sealed class OrderDetailNews : News() {
    data object ShowErrorToast : OrderDetailNews()
    data object OpenPreviousPage : OrderDetailNews()
    data class OpenProductPage(val id: Long) : OrderDetailNews()
}