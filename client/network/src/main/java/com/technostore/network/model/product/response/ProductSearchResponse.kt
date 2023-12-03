package com.technostore.network.model.product.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
class ProductSearchResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("photoLink") val photoLink: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("inCartCount") val inCartCount: Int,
    @SerializedName("reviewCount") val reviewCount: Int,
)