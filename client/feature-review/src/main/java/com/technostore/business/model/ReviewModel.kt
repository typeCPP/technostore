package com.technostore.business.model

data class ReviewModel(
    val id: Long,
    val productId: Long,
    val text: String,
    val rate: Int,
    val date: String,
    val userName: String,
    val photoLink: String,
)