package com.technostore.network.model.order.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class ProductOrderResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("linkPhoto")
    val photoLink: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("name")
    val name: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("count")
    val count: Int,
)