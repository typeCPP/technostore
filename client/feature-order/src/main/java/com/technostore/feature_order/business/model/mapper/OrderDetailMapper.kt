package com.technostore.feature_order.business.model.mapper

import com.technostore.network.model.order.response.OrderDetailResponse

class OrderDetailMapper(private val productOrderMapper: ProductOrderMapper) {
    fun mapFromResponseToModel(data: OrderDetailResponse): com.technostore.feature_order.business.model.OrderDetailModel {
        return com.technostore.feature_order.business.model.OrderDetailModel(
            id = data.id,
            products = data.products?.map { productOrderMapper.mapFromResponseToModel(it) }
        )
    }
}