package com.technostore.network.model.product.response

import com.google.gson.annotations.SerializedName
import com.technostore.network.model.review.response.ReviewResponse

@kotlinx.serialization.Serializable
class ProductDetailResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("linkPhoto") val photoLink: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("userRating") val userRating: Int,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: CategoryResponse,
    @SerializedName("reviews") val reviews: List<ReviewResponse>,
    @SerializedName("inCartCount") val inCartCount: Int,
)