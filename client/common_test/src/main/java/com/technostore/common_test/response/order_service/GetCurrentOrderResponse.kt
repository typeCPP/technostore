package com.technostore.common_test.response.order_service

import com.technostore.common_test.TestData
import org.intellij.lang.annotations.Language

object GetCurrentOrderResponse {
    @Language("JSON")
    const val success = """
        {
          "id": 1,
          "products": [
            {
              "id": ${TestData.FIRST_PRODUCT_ID},
              "linkPhoto": "${TestData.FIRST_PRODUCT_PHOTO_LINK}",
              "price": ${TestData.FIRST_PRODUCT_PRICE},
              "name": "${TestData.FIRST_PRODUCT_NAME}", 
              "rating": ${TestData.FIRST_PRODUCT_RATING}, 
              "count": ${TestData.FIRST_PRODUCT_COUNT}
            }
          ]
        }
    """
}