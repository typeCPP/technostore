package com.technostore.feature_product.business.model


data class ProductDetailModel(
    val id: Long,
    val photoLink: String,
    val name: String,
    val price: Double,
    val rating: Double,
    val userRating: Double,
    val description: String,
    val category: CategoryModel,
    val reviews: List<ReviewModel>
)