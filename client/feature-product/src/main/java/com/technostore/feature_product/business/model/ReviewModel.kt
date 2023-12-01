package com.technostore.feature_product.business.model

data class ReviewModel(
    val id: Long,
    val productId: Long,
    val text: String,
    val rate: Double,
    val date: Double,
    val userName: String,
    val photoLink: String,
)