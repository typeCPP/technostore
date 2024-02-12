package com.technostore.common_test.response.review_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object UserReviewByProductIdResponse {
    @Language("JSON")
    const val success="""
        {
            "id": ${TestData.POSITIVE_REVIEW_ID},
            "productId": ${TestData.POSITIVE_REVIEW_PRODUCT_ID},
            "text": "${TestData.POSITIVE_REVIEW_TEXT}",
            "rate": ${TestData.POSITIVE_REVIEW_RATE},
            "date": ${TestData.POSITIVE_REVIEW_DATE_LONG},
            "userName": "${TestData.POSITIVE_REVIEW_USER_NAME}",
            "photoLink": "${TestData.POSITIVE_REVIEW_PHOTO_LINK}"
        }
    """
}