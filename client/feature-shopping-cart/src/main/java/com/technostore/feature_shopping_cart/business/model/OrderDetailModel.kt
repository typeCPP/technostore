package com.technostore.feature_shopping_cart.business.model

data class OrderDetailModel(
    val id: Long,
    var products: List<ProductOrderModel>,
)