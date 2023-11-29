package com.technostore.feature_profile.orders.presentation

import com.technostore.arch.mvi.News

sealed class OrdersNews : News() {

    data object OpenPreviousPage : OrdersNews()
    class OpenOrderDetail(val id: Long) : OrdersNews()
    data object ShowErrorToast : OrdersNews()
}