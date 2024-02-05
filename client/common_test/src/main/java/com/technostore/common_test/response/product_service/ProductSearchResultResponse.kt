package com.technostore.common_test.response.product_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object ProductSearchResultResponse {

    @Language("JSON")
    const val success = """
        {
          "content": [  
              {
                "id": ${TestData.FIRST_PRODUCT_ID},
                "linkPhoto": "${TestData.FIRST_PRODUCT_PHOTO_LINK}",
                "name": "${TestData.FIRST_PRODUCT_NAME}",
                "price": ${TestData.FIRST_PRODUCT_PRICE},
                "rating": ${TestData.FIRST_PRODUCT_RATING},
                "inCartCount": ${TestData.FIRST_PRODUCT_COUNT},
                "reviewCount": ${TestData.FIRST_PRODUCT_REVIEW_COUNT}
              },{
                "id": ${TestData.SECOND_PRODUCT_ID},
                "linkPhoto": "${TestData.SECOND_PRODUCT_PHOTO_LINK}",
                "name": "${TestData.SECOND_PRODUCT_NAME}",
                "price": ${TestData.SECOND_PRODUCT_PRICE},
                "rating": ${TestData.SECOND_PRODUCT_RATING},
                "inCartCount": ${TestData.SECOND_PRODUCT_COUNT},
                "reviewCount": ${TestData.SECOND_PRODUCT_REVIEW_COUNT}
              }
          ],
          "totalPages": 1
        }
    """
    @Language("JSON")
    const val empty = """
        {
          "content": [],
          "totalPages": 0
        }
    """
}