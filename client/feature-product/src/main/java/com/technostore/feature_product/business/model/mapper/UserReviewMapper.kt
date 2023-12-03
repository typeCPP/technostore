package com.technostore.feature_product.business.model.mapper

import com.technostore.feature_product.business.model.UserReviewModel
import com.technostore.network.model.review.response.UserReviewResponse
import com.technostore.network.utils.URL

class UserReviewMapper {
    fun mapFromResponseToModel(data: UserReviewResponse): UserReviewModel {
        return UserReviewModel(
            id = data.id,
            productId = data.productId,
            text = data.text,
            rate = data.rate,
            date = data.date,
            photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${data.photoLink}",
        )
    }
}