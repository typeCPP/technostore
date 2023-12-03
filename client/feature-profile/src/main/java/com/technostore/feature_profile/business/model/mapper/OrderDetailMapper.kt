package com.technostore.feature_profile.business.model.mapper

import com.technostore.feature_profile.business.model.OrderDetailModel
import com.technostore.network.model.order.response.OrderDetailResponse

class OrderDetailMapper(private val productOrderMapper: ProductOrderMapper) {
    fun mapFromResponseToModel(data: OrderDetailResponse): OrderDetailModel {
        return OrderDetailModel(
            id = data.id,
            products = data.products?.map { productOrderMapper.mapFromResponseToModel(it) }
        )
    }
}