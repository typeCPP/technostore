package com.technostore.network.model.product.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class CategoryResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
)