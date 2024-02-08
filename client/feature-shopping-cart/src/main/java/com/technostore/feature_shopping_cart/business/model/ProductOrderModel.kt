package com.technostore.feature_shopping_cart.business.model

data class ProductOrderModel(
    val id: Long,
    val photoLink: String,
    val price: Double,
    val name: String,
    val rating: Double,
    var count: Int,
)