package com.technostore.network.model.product.response

import com.google.gson.annotations.SerializedName
import com.technostore.network.model.review.response.ReviewResponse

@kotlinx.serialization.Serializable
class ProductDetail(
    @SerializedName("id") val id: Long,
    @SerializedName("photoLink") val photoLink: String,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("userRating") val userRating: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: CategoryResponse,
    @SerializedName("reviews") val reviews: List<ReviewResponse>
)