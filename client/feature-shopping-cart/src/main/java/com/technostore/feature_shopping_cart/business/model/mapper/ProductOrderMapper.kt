package com.technostore.feature_shopping_cart.business.model.mapper

import com.technostore.feature_shopping_cart.business.model.ProductOrderModel
import com.technostore.network.model.order.response.ProductOrderResponse
import com.technostore.network.utils.URL.BASE_URL
import com.technostore.network.utils.URL.ORDER_SERVICE_BASE_URL

class ProductOrderMapper {
    fun mapFromResponseToModel(data: ProductOrderResponse): ProductOrderModel {
        return ProductOrderModel(
            id = data.id,
            photoLink = "$BASE_URL$ORDER_SERVICE_BASE_URL${data.photoLink}",
            price = data.price,
            name = data.name,
            rating = data.rating,
            count = data.count
        )
    }
}