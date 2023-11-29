package com.technostore.feature_profile.business.model

data class OrderDetailModel(
    val id: Long,
    val products: List<ProductOrderModel>,
)