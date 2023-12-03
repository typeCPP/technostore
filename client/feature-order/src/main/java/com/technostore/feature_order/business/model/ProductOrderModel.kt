package com.technostore.feature_order.business.model

data class ProductOrderModel(
    val id: Long,
    val photoLink: String,
    val price: Double,
    val name: String,
    val rating: Double,
    val count: Int,
)