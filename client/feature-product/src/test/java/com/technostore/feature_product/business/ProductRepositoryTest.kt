package com.technostore.feature_product.business

import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.OrderServiceMock
import com.technostore.common_test.mock.ProductServiceMock
import com.technostore.common_test.mock.ReviewServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_product.business.model.CategoryModel
import com.technostore.feature_product.business.model.ProductDetailModel
import com.technostore.feature_product.business.model.ReviewModel
import com.technostore.feature_product.business.model.UserReviewModel
import com.technostore.feature_product.business.model.mapper.CategoryMapper
import com.technostore.feature_product.business.model.mapper.ProductDetailMapper
import com.technostore.feature_product.business.model.mapper.ReviewMapper
import com.technostore.feature_product.business.model.mapper.UserReviewMapper
import com.technostore.network.utils.URL
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProductRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()
    private val reviewMapper = ReviewMapper()
    private val categoryMapper = CategoryMapper()
    private val productDetailMapper = ProductDetailMapper(
        categoryMapper = categoryMapper,
        reviewMapper = reviewMapper
    )
    private val userReviewMapper = UserReviewMapper()
    private val productRepository = ProductRepositoryImpl(
        productService = networkModule.productService,
        reviewService = networkModule.reviewService,
        orderService = networkModule.orderService,
        productDetailMapper = productDetailMapper,
        userReviewMapper = userReviewMapper,
        reviewMapper = reviewMapper
    )

    private val firstExpectedReviewModel = ReviewModel(
        id = TestData.POSITIVE_REVIEW_ID,
        productId = TestData.POSITIVE_REVIEW_PRODUCT_ID,
        text = TestData.POSITIVE_REVIEW_TEXT,
        rate = TestData.POSITIVE_REVIEW_RATE,
        date = TestData.POSITIVE_REVIEW_DATE_LONG,
        photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.POSITIVE_REVIEW_PHOTO_LINK}",
        userName = TestData.POSITIVE_REVIEW_USER_NAME
    )
    private val secondExpectedReviewModel = ReviewModel(
        id = TestData.NEUTRAL_REVIEW_ID,
        productId = TestData.NEUTRAL_REVIEW_PRODUCT_ID,
        text = TestData.NEUTRAL_REVIEW_TEXT,
        rate = TestData.NEUTRAL_REVIEW_RATE,
        date = TestData.NEUTRAL_REVIEW_DATE_LONG,
        photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.NEUTRAL_REVIEW_PHOTO_LINK}",
        userName = TestData.NEUTRAL_REVIEW_USER_NAME
    )

    /* get product by id */
    @Test
    fun `get product with 200 status → return success with product model`() = runTest {
        ProductServiceMock {
            product.success()
        }
        val expectedCategoryModel = CategoryModel(
            id = TestData.FIRST_CATEGORY_ID,
            name = TestData.FIRST_CATEGORY_NAME
        )
        val expectedModel = ProductDetailModel(
            id = TestData.FIRST_PRODUCT_ID,
            photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
            name = TestData.FIRST_PRODUCT_NAME,
            price = TestData.FIRST_PRODUCT_PRICE,
            rating = TestData.FIRST_PRODUCT_RATING,
            userRating = TestData.USER_REVIEW_RATE,
            description = TestData.FIRST_PRODUCT_DESCRIPTION,
            inCartCount = TestData.FIRST_PRODUCT_COUNT,
            category = expectedCategoryModel,
            reviews = listOf(firstExpectedReviewModel, secondExpectedReviewModel)
        )
        val result = productRepository.getProductById(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Success && result.data == expectedModel)
    }

    @Test
    fun `get product with 500 status → return error`() = runTest {
        ProductServiceMock {
            product.internalError()
        }
        val result = productRepository.getProductById(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Error)
    }

    /* setReview */
    @Test
    fun `set review with null text return 200 status → return success`() = runTest {
        ReviewServiceMock {
            setReview.success()
        }
        val result = productRepository.setReview(
            productId = TestData.FIRST_PRODUCT_ID,
            rating = TestData.USER_REVIEW_RATE,
            text = null
        )
        assertTrue(result is Result.Success)
    }

    @Test
    fun `set review with not empty text return 200 status → return success`() = runTest {
        ReviewServiceMock {
            setReview.success()
        }
        val result = productRepository.setReview(
            productId = TestData.FIRST_PRODUCT_ID,
            rating = TestData.USER_REVIEW_RATE,
            text = TestData.USER_REVIEW_TEXT
        )
        assertTrue(result is Result.Success)
    }

    @Test
    fun `set review return 500 status → return error`() = runTest {
        ReviewServiceMock {
            setReview.internalError()
        }
        val result = productRepository.setReview(
            productId = TestData.FIRST_PRODUCT_ID,
            rating = TestData.USER_REVIEW_RATE,
            text = TestData.USER_REVIEW_TEXT
        )
        assertTrue(result is Result.Error)
    }

    /* setProductCount */
    @Test
    fun `set product count return 200 status → return success`() = runTest {
        OrderServiceMock {
            setProductCount.success()
        }
        val result = productRepository.setProductCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = TestData.FIRST_PRODUCT_COUNT
        )
        assertTrue(result is Result.Success)
    }

    @Test
    fun `set product count return 500 status → return error`() = runTest {
        OrderServiceMock {
            setProductCount.internalError()
        }
        val result = productRepository.setProductCount(
            productId = TestData.FIRST_PRODUCT_ID,
            count = TestData.FIRST_PRODUCT_COUNT
        )
        assertTrue(result is Result.Error)
    }

    /* getUserReview */
    @Test
    fun `get user review with 200 status → return success with review model`() = runTest {
        ReviewServiceMock {
            userReviewByProductId.success()
        }
        val expectedModel = UserReviewModel(
            id = TestData.POSITIVE_REVIEW_ID,
            productId = TestData.POSITIVE_REVIEW_PRODUCT_ID,
            text = TestData.POSITIVE_REVIEW_TEXT,
            rate = TestData.POSITIVE_REVIEW_RATE,
            date = TestData.POSITIVE_REVIEW_DATE_LONG,
            photoLink = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.POSITIVE_REVIEW_PHOTO_LINK}",
        )
        val result = productRepository.getUserReview(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Success && result.data == expectedModel)
    }

    @Test
    fun `get user review with 404 status → return success with empty body`() = runTest {
        ReviewServiceMock {
            userReviewByProductId.notFound()
        }
        val result = productRepository.getUserReview(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Success && result.data == null)
    }

    @Test
    fun `get user review with 500 status → return error`() = runTest {
        ReviewServiceMock {
            userReviewByProductId.internalError()
        }
        val result = productRepository.getUserReview(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Error && result.error == null)
    }

    /* getReviews */
    @Test
    fun `get reviews with 200 status → return success with reviews model`() = runTest {
        ReviewServiceMock {
            reviewsByProductId.success()
        }
        val expectedResult = listOf(firstExpectedReviewModel, secondExpectedReviewModel)
        val result = productRepository.getReviews(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Success && result.data == expectedResult)
    }

    @Test
    fun `get reviews with 500 status → return error`() = runTest {
        ReviewServiceMock {
            reviewsByProductId.internalError()
        }
        val result = productRepository.getReviews(TestData.FIRST_PRODUCT_ID)
        assertTrue(result is Result.Error && result.error == null)
    }
}