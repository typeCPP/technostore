package com.technostore.common_test.response.product_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object ProductResponse {

    @Language("JSON")
    const val success = """
        {
            "id": ${TestData.FIRST_PRODUCT_ID},
            "linkPhoto": "${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            "name": "${TestData.FIRST_PRODUCT_NAME}",
            "price": ${TestData.FIRST_PRODUCT_PRICE},
            "rating": ${TestData.FIRST_PRODUCT_RATING},
            "userRating": ${TestData.USER_REVIEW_RATE},
            "description": "${TestData.FIRST_PRODUCT_DESCRIPTION}",
            "category": {
              "id": ${TestData.FIRST_CATEGORY_ID}, 
              "name": "${TestData.FIRST_CATEGORY_NAME}"
            },
            "reviews": [
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
            ],
            "inCartCount": ${TestData.FIRST_PRODUCT_COUNT}
            }
    """
}