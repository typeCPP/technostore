package com.technostore.network.model.order.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class OrderDetailResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("products")
    val products: List<ProductOrderResponse>,
)