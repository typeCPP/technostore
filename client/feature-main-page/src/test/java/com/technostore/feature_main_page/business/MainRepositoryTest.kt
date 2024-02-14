package com.technostore.feature_main_page.business

import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.ProductServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.business.model.mapper.ProductSearchMapper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()

    private val productSearchMapper = ProductSearchMapper()
    private val mainRepository = MainRepositoryImpl(
        productService = networkModule.productService,
        productSearchMapper = productSearchMapper
    )
    private val firstProductModel = ProductSearchModel(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.FIRST_PRODUCT_PHOTO_LINK}",
        price = TestData.FIRST_PRODUCT_PRICE,
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        inCartCount = TestData.FIRST_PRODUCT_COUNT,
        reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
    )
    private val secondProductModel = ProductSearchModel(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = "${URL.BASE_URL}${URL.PRODUCT_SERVICE_BASE_URL}${TestData.SECOND_PRODUCT_PHOTO_LINK}",
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        inCartCount = TestData.SECOND_PRODUCT_COUNT,
        reviewCount = TestData.SECOND_PRODUCT_REVIEW_COUNT
    )
    private val products = listOf(firstProductModel, secondProductModel)

    /* searchByPopularity */
    @Test
    fun `search by popularity with 200 status → return success with data`() = runTest {
        ProductServiceMock {
            searchProducts.success()
        }
        val result = mainRepository.searchByPopularity()
        assertTrue(result is Result.Success && result.data == products)
    }

    @Test
    fun `search by popularity with 200 status  end empty body → return error`() = runTest {
        ProductServiceMock {
            searchProducts.emptyBody()
        }
        val result = mainRepository.searchByPopularity()
        assertTrue(result is Result.Error)
    }

    @Test
    fun `search by popularity with 500 status → return error`() = runTest {
        ProductServiceMock {
            searchProducts.internalError()
        }
        val result = mainRepository.searchByPopularity()
        assertTrue(result is Result.Error)
    }

    /* searchByCategory */
    @Test
    fun `search by catergory with 200 status → return success with data`() = runTest {
        ProductServiceMock {
            searchProducts.success()
        }
        val result = mainRepository.searchByCategory(TestData.FIRST_CATEGORY_ID)
        assertTrue(result is Result.Success && result.data == products)
    }

    @Test
    fun `search products with 200 status end empty body → return error`() = runTest {
        ProductServiceMock {
            searchProducts.emptyBody()
        }
        val result = mainRepository.searchByCategory(TestData.FIRST_CATEGORY_ID)
        assertTrue(result is Result.Error)
    }

    @Test
    fun `search products with 500 status → return error`() = runTest {
        ProductServiceMock {
            searchProducts.internalError()
        }
        val result = mainRepository.searchByCategory(TestData.FIRST_CATEGORY_ID)
        assertTrue(result is Result.Error)
    }
}