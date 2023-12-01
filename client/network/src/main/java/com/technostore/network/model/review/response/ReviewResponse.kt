package com.technostore.network.model.review.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class ReviewResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("productId") val productId: Long,
    @SerializedName("text") val text: String,
    @SerializedName("rate") val rate: Int,
    @SerializedName("date") val date: Long,
    @SerializedName("userName") val userName: String,
    @SerializedName("photoLink") val photoLink: String,
)