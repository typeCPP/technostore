package com.technostore.feature_order.business.model

data class OrderDetailModel(
    val id: Long,
    val products: List<ProductOrderModel>?,
)