package com.technostore.feature_product.product.business

import com.technostore.common_test.TestData
import com.technostore.feature_product.business.model.CategoryModel
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.feature_product.business.model.UserReviewModel

open class ProductBaseTest {
    protected val categoryModel = CategoryModel(
        id = TestData.FIRST_CATEGORY_ID,
        name = TestData.FIRST_CATEGORY_NAME
    )
    protected val userReview = UserReviewModel(
        id = TestData.USER_REVIEW_ID,
        productId = TestData.FIRST_PRODUCT_ID,
        text = TestData.USER_REVIEW_TEXT,
        rate = TestData.USER_REVIEW_RATE,
        date = TestData.USER_REVIEW_DATE,
        photoLink = TestData.USER_REVIEW_PHOTO_LINK
    )
    protected val reviewModel = ReviewModel(
        id = TestData.USER_REVIEW_ID,
        productId = TestData.FIRST_PRODUCT_ID,
        text = TestData.USER_REVIEW_TEXT,
        rate = TestData.USER_REVIEW_RATE,
        date = TestData.USER_REVIEW_DATE,
        photoLink = TestData.USER_REVIEW_PHOTO_LINK,
        userName = TestData.NAME
    )
    protected val productDetailModel = ProductDetailModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = TestData.FIRST_PRODUCT_PHOTO_LINK,
        name = TestData.FIRST_PRODUCT_NAME,
        price = TestData.FIRST_PRODUCT_PRICE,
        rating = TestData.FIRST_PRODUCT_RATING,
        userRating = 0,
        description = TestData.FIRST_PRODUCT_DESCRIPTION,
        category = categoryModel,
        reviews = listOf(),
        inCartCount = TestData.FIRST_PRODUCT_COUNT
    )
}