package com.technostore.common_test.response.review_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object ReviewsByProductIdResponse {
    @Language("JSON")
    const val success="""
        [
                {
                  "id": ${TestData.POSITIVE_REVIEW_ID},
                  "productId": ${TestData.POSITIVE_REVIEW_PRODUCT_ID},
                  "text": "${TestData.POSITIVE_REVIEW_TEXT}",
                  "rate": ${TestData.POSITIVE_REVIEW_RATE},
                  "date": ${TestData.POSITIVE_REVIEW_DATE_LONG},
                  "userName": "${TestData.POSITIVE_REVIEW_USER_NAME}",
                  "photoLink": "${TestData.POSITIVE_REVIEW_PHOTO_LINK}"
                },
                            {
                  "id": ${TestData.NEUTRAL_REVIEW_ID},
                  "productId": ${TestData.NEUTRAL_REVIEW_PRODUCT_ID},
                  "text": "${TestData.NEUTRAL_REVIEW_TEXT}",
                  "rate": ${TestData.NEUTRAL_REVIEW_RATE},
                  "date": ${TestData.NEUTRAL_REVIEW_DATE_LONG},
                  "userName": "${TestData.NEUTRAL_REVIEW_USER_NAME}",
                  "photoLink": "${TestData.NEUTRAL_REVIEW_PHOTO_LINK}"
                }
            ]
    """
}