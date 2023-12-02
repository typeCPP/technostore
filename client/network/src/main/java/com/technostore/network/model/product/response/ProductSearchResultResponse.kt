package com.technostore.network.model.product.response

import com.google.gson.annotations.SerializedName

@kotlinx.serialization.Serializable
data class ProductSearchResultResponse(
    @SerializedName("content")
    val listOfProducts: List<ProductSearchResponse>,
    @SerializedName("totalPages")
    val totalPages: Int
)