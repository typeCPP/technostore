package com.technostore.feature_order.orders.presentation

import com.technostore.arch.mvi.News

sealed class OrdersNews : News() {

    data object OpenPreviousPage : OrdersNews()
    class OpenOrderDetail(val id: Long) : OrdersNews()
    data object ShowErrorToast : OrdersNews()
    data object OpenMainPage : OrdersNews()
}