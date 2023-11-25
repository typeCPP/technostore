package com.technostore.shared_search.business.model

data class CategoryWithCheck(
    val category: Category,
    var isSelected: Boolean = false
)

data class Category(
    val id: Long,
    val name: String,
)