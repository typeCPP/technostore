package com.technostore.feature_review.business.model.mapper

import android.annotation.SuppressLint
import com.technostore.feature_review.business.model.ReviewModel
import com.technostore.network.model.review.response.ReviewResponse
import com.technostore.network.utils.URL
import java.text.SimpleDateFormat
import java.util.Date

class ReviewMapper {
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("dd.MM.yyyy")
    fun mapFromResponseToModel(data: ReviewResponse): ReviewModel {
        return ReviewModel(
            id = data.id,
            productId = data.productId,
            text = data.text,
            rate = data.rate,
            date = formatter.format(Date(data.date)),
            userName = data.userName,
            photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${data.photoLink}",
        )
    }
}