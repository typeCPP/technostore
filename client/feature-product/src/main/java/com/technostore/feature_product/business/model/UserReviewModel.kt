package com.technostore.feature_product.business.model

data class UserReviewModel(
    val id: Long,
    val productId: Long,
    val text: String?,
    val rate: Int,
    val date: Long,
    val photoLink: String,
)