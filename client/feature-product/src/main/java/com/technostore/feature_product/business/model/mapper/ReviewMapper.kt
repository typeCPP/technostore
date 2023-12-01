package com.technostore.feature_product.business.model.mapper

import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.network.model.review.response.ReviewResponse
import com.technostore.network.utils.URL

class ReviewMapper {
    fun mapFromResponseToModel(data: ReviewResponse): ReviewModel {
        return ReviewModel(
            id = data.id,
            productId = data.productId,
            text = data.text,
            rate = data.rate,
            date = data.date,
            userName = data.userName,
            photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${data.photoLink}",
        )
    }
}