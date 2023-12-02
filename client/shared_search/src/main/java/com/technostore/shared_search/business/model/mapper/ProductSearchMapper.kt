package com.technostore.shared_search.business.model.mapper

import com.technostore.network.model.product.response.ProductSearchResponse
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.model.ProductSearchModel

class ProductSearchMapper {
    fun mapFromResponseToModel(data: ProductSearchResponse): ProductSearchModel {
        return ProductSearchModel(
            id = data.id,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${data.photoLink}",
            price = data.price,
            name = data.name,
            rating = data.rating,
            inCartCount = data.inCartCount
        )
    }
}