package com.technostore.shared_search.business

import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.OrderServiceMock
import com.technostore.common_test.mock.ProductServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.network.model.product.response.ProductSearchResponse
import com.technostore.network.model.product.response.ProductSearchResultResponse
import com.technostore.network.service.ProductService
import com.technostore.network.utils.URL
import com.technostore.shared_search.business.error.SearchEmpty
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.business.model.mapper.ProductSearchMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SharedSearchRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()
    private val productSearchMapper = ProductSearchMapper()

    private val sharedSearchRepository = SharedSearchRepositoryImpl(
        productService = networkModule.productService,
        orderService = networkModule.orderService,
        productSearchMapper = productSearchMapper
    )

    private val testScope = TestScope()

    private val firstCategory = Category(
        id = TestData.FIRST_CATEGORY_ID,
        name = TestData.FIRST_CATEGORY_NAME
    )
    private val secondCategory = Category(
        id = TestData.SECOND_CATEGORY_ID,
        name = TestData.SECOND_CATEGORY_NAME
    )
    private val firstCategoryWithCheck = CategoryWithCheck(
        category = firstCategory,
        isSelected = false
    )
    private val secondCategoryWithCheck = CategoryWithCheck(
        category = secondCategory,
        isSelected = false
    )
    private val expectedCategoryWithCheckModels = listOf(
        firstCategoryWithCheck,
        secondCategoryWithCheck
    )
    private val defaultNumberPage = 0
    private val defaultMinRating = 0
    private val defaultMaxRating = 10
    private val defaultMinPrice = 1
    private val defaultMaxPrice = 1000000
    private val defaultCategories = null

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

    private val firstProductResponse = ProductSearchResponse(
        id = TestData.FIRST_PRODUCT_ID,
        photoLink = TestData.FIRST_PRODUCT_PHOTO_LINK,
        price = TestData.FIRST_PRODUCT_PRICE,
        name = TestData.FIRST_PRODUCT_NAME,
        rating = TestData.FIRST_PRODUCT_RATING,
        inCartCount = TestData.FIRST_PRODUCT_COUNT,
        reviewCount = TestData.FIRST_PRODUCT_REVIEW_COUNT
    )
    private val secondProductResponse = ProductSearchResponse(
        id = TestData.SECOND_PRODUCT_ID,
        photoLink = TestData.SECOND_PRODUCT_PHOTO_LINK,
        price = TestData.SECOND_PRODUCT_PRICE,
        name = TestData.SECOND_PRODUCT_NAME,
        rating = TestData.SECOND_PRODUCT_RATING,
        inCartCount = TestData.SECOND_PRODUCT_COUNT,
        reviewCount = TestData.SECOND_PRODUCT_REVIEW_COUNT
    )
    private val serviceMock = mockk<ProductService>(relaxed = true) {
        coEvery {
            searchProducts(any(), any(), any(), any(), any(), any(), any(), any(), any())
        } returns Response.success(
            ProductSearchResultResponse(
                listOfProducts = listOf(
                    firstProductResponse,
                    secondProductResponse
                ),
                totalPages = 1
            )
        )
    }
    private val sharedRepositoryServiceMock = SharedSearchRepositoryImpl(
        productService = serviceMock,
        orderService = networkModule.orderService,
        productSearchMapper = productSearchMapper
    )

    /* get popular categories */
    @Test
    fun `get categories with 200 status → return success with categories model`() =
        testScope.runTest {
            ProductServiceMock {
                popularCategories.success()
            }
            val result = sharedSearchRepository.getCategories()
            assertTrue(result is Result.Success && result.data == expectedCategoryWithCheckModels)
        }

    @Test
    fun `get categories with 500 status → return error`() =
        testScope.runTest {
            ProductServiceMock {
                popularCategories.internalError()
            }
            val result = sharedSearchRepository.getCategories()
            assertTrue(result is Result.Error)
        }

    /* isSelectedByPopularity */
    @Test
    fun `isSelectedByPopularity = false, call setIsSelectedByPopularity → set isSelectedByRating = false, set isSelectedByPopularity = true`() =
        testScope.runTest {
            val isSelectedByPopularity = sharedSearchRepository.setIsSelectByPopularity()
            val isSelectedByRating = sharedSearchRepository.getIsSortByRating()
            assertTrue(!isSelectedByRating && isSelectedByPopularity)
        }

    @Test
    fun `isSelectedByPopularity = true, call setIsSelectedByPopularity → set isSelectedByPopularity = false`() =
        testScope.runTest {
            val isSelectedByPopularity = sharedSearchRepository.setIsSelectByPopularity()
            assertTrue(isSelectedByPopularity)

            val newIsSelectedByPopularity = sharedSearchRepository.setIsSelectByPopularity()
            assertTrue(!newIsSelectedByPopularity)
        }

    /* isSelectedByRating */
    @Test
    fun `isSelectedByRating = false, call setIsSelectedByRating → set isSelectedByPopularity = false, set isSelectedByRating = true`() =
        testScope.runTest {
            val isSelectedByRating = sharedSearchRepository.setIsSelectByRating()
            val isSelectedByPopularity = sharedSearchRepository.getIsSortByPopularity()
            assertTrue(!isSelectedByPopularity && isSelectedByRating)
        }

    @Test
    fun `isSelectedByRating = true, call isSelectedByPopularity → set isSelectedByRating = false`() =
        testScope.runTest {
            val isSelectedByRating = sharedSearchRepository.setIsSelectByRating()
            assertTrue(isSelectedByRating)

            val newIsSelectedByRating = sharedSearchRepository.setIsSelectByRating()
            assertTrue(!newIsSelectedByRating)
        }

    /* updateSelectedCategories */
    @Test
    fun `categoryWithCheck isSelected = true, category does not exists, updateSelectedCategories → add to selected categories`() =
        testScope.runTest {
            val categoryWithCheck = firstCategoryWithCheck.copy(isSelected = true)
            sharedSearchRepository.updateSelectedCategories(categoryWithCheck)
            val selectedCategories = sharedSearchRepository.getSelectedCategories()
            assertTrue(selectedCategories.size == 1 && selectedCategories[0] == categoryWithCheck.category.id)
        }

    @Test
    fun `categoryWithCheck isSelected = true, category exists, updateSelectedCategories → category is not being added`() =
        testScope.runTest {
            val categoryWithCheck = firstCategoryWithCheck.copy(isSelected = true)
            sharedSearchRepository.updateSelectedCategories(categoryWithCheck)
            val selectedCategories = sharedSearchRepository.getSelectedCategories()
            assertTrue(selectedCategories.size == 1 && selectedCategories[0] == categoryWithCheck.category.id)

            sharedSearchRepository.updateSelectedCategories(categoryWithCheck)
            val newSelectedCategories = sharedSearchRepository.getSelectedCategories()
            assertTrue(newSelectedCategories.size == selectedCategories.size && newSelectedCategories[0] == selectedCategories[0])
        }

    @Test
    fun `categoryWithCheck isSelected = false, category exists, updateSelectedCategories → remove from selected categories`() =
        testScope.runTest {
            val categoryWithCheck = firstCategoryWithCheck.copy(isSelected = true)
            sharedSearchRepository.updateSelectedCategories(categoryWithCheck)
            val selectedCategories = sharedSearchRepository.getSelectedCategories()
            assertTrue(selectedCategories.size == 1 && selectedCategories[0] == categoryWithCheck.category.id)

            sharedSearchRepository.updateSelectedCategories(firstCategoryWithCheck)
            assertTrue(selectedCategories.size == 0)
        }

    @Test
    fun `categoryWithCheck isSelected = false, category does not exists, updateSelectedCategories → category is not being deleted`() =
        testScope.runTest {
            val categoryWithCheck = firstCategoryWithCheck.copy(isSelected = true)
            sharedSearchRepository.updateSelectedCategories(categoryWithCheck)
            val selectedCategories = sharedSearchRepository.getSelectedCategories()
            assertTrue(selectedCategories.size == 1 && selectedCategories[0] == categoryWithCheck.category.id)

            sharedSearchRepository.updateSelectedCategories(secondCategoryWithCheck)
            assertTrue(selectedCategories.size == 1 && selectedCategories[0] == categoryWithCheck.category.id)
        }

    /* updateCostBoundaries */
    @Test
    fun `new minCost = 2, maxCost = 4 → update minCost, maxCost`() =
        testScope.runTest {
            sharedSearchRepository.updateCostBoundaries(2f, 4f)
            val minPrice = sharedSearchRepository.getMinPrice()
            val maxPrice = sharedSearchRepository.getMaxPrice()
            assertTrue(minPrice == 2f && maxPrice == 4f)
        }

    @Test
    fun `new minCost = 5, maxCost = 3 → minCost, maxCost do not updated`() =
        testScope.runTest {
            sharedSearchRepository.updateCostBoundaries(2f, 4f)
            val minPrice = sharedSearchRepository.getMinPrice()
            val maxPrice = sharedSearchRepository.getMaxPrice()
            assertTrue(minPrice == 2f && maxPrice == 4f)

            sharedSearchRepository.updateCostBoundaries(5f, 3f)
            val newMinPrice = sharedSearchRepository.getMinPrice()
            val newMaxPrice = sharedSearchRepository.getMaxPrice()
            assertTrue(newMinPrice == 2f && newMaxPrice == 4f)
        }

    @Test
    fun `new minRating = 3, maxRaing = 5 → update minRating, maxRaing`() =
        testScope.runTest {
            sharedSearchRepository.updateRatingBoundaries(3f, 5f)
            val minRating = sharedSearchRepository.getMinRating()
            val maxRating = sharedSearchRepository.getMaxRating()
            assertTrue(minRating == 3f && maxRating == 5f)
        }

    /* updateRatingBoundaries */
    @Test
    fun `new minRating = 4, maxRaing = 2 → minRating, maxRaing do not updated`() =
        testScope.runTest {
            sharedSearchRepository.updateRatingBoundaries(3f, 5f)
            val minRating = sharedSearchRepository.getMinRating()
            val maxRating = sharedSearchRepository.getMaxRating()
            assertTrue(minRating == 3f && maxRating == 5f)

            sharedSearchRepository.updateRatingBoundaries(4f, 2f)
            val newMinRating = sharedSearchRepository.getMinRating()
            val newMaxRating = sharedSearchRepository.getMaxRating()
            assertTrue(newMinRating == 3f && newMaxRating == 5f)
        }

    /* setProductCount */
    @Test
    fun `set productCount = 2, request is successfull → return success`() = testScope.runTest {
        OrderServiceMock {
            setProductCount.success()
        }
        val result = sharedSearchRepository.setProductCount(1, 2)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `set productCount = 2, request return 500 code → return error`() = testScope.runTest {
        OrderServiceMock {
            setProductCount.internalError()
        }
        val result = sharedSearchRepository.setProductCount(1, 2)
        assertTrue(result is Result.Error)
    }

    /* searchProducts */
    @Test
    fun `word is not empty, isSearchByPopularity = false, isSearchByRating = false, categories is empty, request return not empty list → return success with models`() =
        testScope.runTest {
            ProductServiceMock {
                searchProducts.success()
            }
            val word = "word"
            val expectedData = listOf(firstProductModel, secondProductModel)
            val result = sharedSearchRepository.searchProducts(word)
            assertTrue(result is Result.Success && result.data == expectedData)
        }

    @Test
    fun `word is not empty, request return empty list → return search empty error`() =
        testScope.runTest {
            ProductServiceMock {
                searchProducts.empty()
            }
            val result = sharedSearchRepository.searchProducts("word")
            assertTrue(result is Result.Error && result.error is SearchEmpty)
        }

    @Test
    fun `word is not empty, request return error → return search error`() =
        testScope.runTest {
            ProductServiceMock {
                searchProducts.internalError()
            }
            val result = sharedSearchRepository.searchProducts("word")
            assertTrue(result is Result.Error && result.error == null)
        }


    /* with mock service */
    @Test
    fun `word is not empty, isSearchByPopularity = true, isSearchByRating = false, categories is empty, request return not empty list → set sort by popularity`() =
        testScope.runTest {
            val word = "word"
            sharedRepositoryServiceMock.setIsSelectByPopularity()
            sharedRepositoryServiceMock.searchProducts(word)
            coVerify(exactly = 1) {
                serviceMock.searchProducts(
                    numberPage = defaultNumberPage,
                    sizePage = PAGE_SIZE,
                    word = word,
                    sort = SEARCH_BY_POPULARITY,
                    minRating = defaultMinRating,
                    maxRating = defaultMaxRating,
                    minPrice = defaultMinPrice,
                    maxPrice = defaultMaxPrice,
                    categories = defaultCategories
                )
            }
        }

    @Test
    fun `word is not empty, isSearchByPopularity = false, isSearchByRating = false, categories is not empty, request return not empty list → call response with selected category, sortingType = nothing`() =
        testScope.runTest {
            sharedRepositoryServiceMock.updateSelectedCategories(
                firstCategoryWithCheck.copy(
                    isSelected = true
                )
            )
            val word = "word"
            sharedRepositoryServiceMock.searchProducts(word)
            coVerify(exactly = 1) {
                serviceMock.searchProducts(
                    numberPage = defaultNumberPage,
                    sizePage = PAGE_SIZE,
                    word = word,
                    sort = SEARCH_NOTHING,
                    minRating = defaultMinRating,
                    maxRating = defaultMaxRating,
                    minPrice = defaultMinPrice,
                    maxPrice = defaultMaxPrice,
                    categories = listOf(TestData.FIRST_CATEGORY_ID).joinToString(",")
                )
            }
        }

    @Test
    fun `word is not empty, isSearchByPopularity = false, isSearchByRating = true, categories is empty, request return not empty list → set sort by rating`() =
        testScope.runTest {
            val word = "word"
            sharedRepositoryServiceMock.setIsSelectByRating()
            sharedRepositoryServiceMock.searchProducts(word)
            coVerify(exactly = 1) {
                serviceMock.searchProducts(
                    numberPage = defaultNumberPage,
                    sizePage = PAGE_SIZE,
                    word = word,
                    sort = SEARCH_BY_RATING,
                    minRating = defaultMinRating,
                    maxRating = defaultMaxRating,
                    minPrice = defaultMinPrice,
                    maxPrice = defaultMaxPrice,
                    categories = defaultCategories
                )
            }
        }

    @Test
    fun `word is repeating, repeat request with equal word, response return success → update numberPage`() =
        testScope.runTest {
            val word = "word"
            sharedRepositoryServiceMock.searchProducts(word)
            coVerify(exactly = 1) {
                serviceMock.searchProducts(
                    numberPage = defaultNumberPage,
                    sizePage = PAGE_SIZE,
                    word = word,
                    sort = SEARCH_NOTHING,
                    minRating = defaultMinRating,
                    maxRating = defaultMaxRating,
                    minPrice = defaultMinPrice,
                    maxPrice = defaultMaxPrice,
                    categories = defaultCategories
                )
            }
            sharedRepositoryServiceMock.searchProducts(word)
            coVerify(exactly = 1) {
                serviceMock.searchProducts(
                    numberPage = 1,
                    sizePage = PAGE_SIZE,
                    word = word,
                    sort = SEARCH_NOTHING,
                    minRating = defaultMinRating,
                    maxRating = defaultMaxRating,
                    minPrice = defaultMinPrice,
                    maxPrice = defaultMaxPrice,
                    categories = defaultCategories
                )
            }
        }
}