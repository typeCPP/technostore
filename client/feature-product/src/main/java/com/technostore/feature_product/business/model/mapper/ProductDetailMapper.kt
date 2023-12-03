package com.technostore.feature_product.business.model.mapper

import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.network.model.product.response.ProductDetailResponse
import com.technostore.network.utils.URL

class ProductDetailMapper(
    private val categoryMapper: CategoryMapper,
    private val reviewMapper: ReviewMapper
) {
    fun mapFromResponseToModel(data: ProductDetailResponse): ProductDetailModel {
        return ProductDetailModel(
            id = data.id,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${data.photoLink}",
            price = data.price,
            rating = data.rating,
            userRating = data.userRating,
            description = data.description,
            category = categoryMapper.mapFromResponseToModel(data.category),
            reviews = data.reviews.map { reviewMapper.mapFromResponseToModel(it) },
            name = data.name,
            inCartCount = data.inCartCount
        )
    }
}