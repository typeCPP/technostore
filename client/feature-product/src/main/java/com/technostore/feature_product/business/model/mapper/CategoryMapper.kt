package com.technostore.feature_product.business.model.mapper

import com.technostore.feature_product.business.model.CategoryModel
import com.technostore.network.model.product.response.CategoryResponse

class CategoryMapper {
    fun mapFromResponseToModel(data: CategoryResponse): CategoryModel {
        return CategoryModel(
            id = data.id,
            name = data.name
        )
    }
}