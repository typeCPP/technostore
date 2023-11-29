package com.technostore.network.model.order.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class Order(
    @SerializedName("id")
    val id: Long
)