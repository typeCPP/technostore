package com.technostore.shared_search.business.model

data class ProductSearchModel(
    val id: Long,
    val photoLink: String,
    val name: String,
    val price: Double,
    val rating: Double,
    var inCartCount: Int?,
)