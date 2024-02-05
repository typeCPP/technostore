package com.technostore.shared_search.business

import com.technostore.arch.result.Result
import com.technostore.network.service.OrderService
import com.technostore.network.service.ProductService
import com.technostore.shared_search.business.error.SearchEmpty
import com.technostore.shared_search.business.model.Category
import com.technostore.shared_search.business.model.CategoryWithCheck
import com.technostore.shared_search.business.model.ProductSearchModel
import com.technostore.shared_search.business.model.mapper.ProductSearchMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val PAGE_SIZE = 100

const val SEARCH_BY_POPULARITY = "BY_POPULARITY"
const val SEARCH_BY_RATING = "BY_RATING"
const val SEARCH_NOTHING = "NOTHING"

class SharedSearchRepositoryImpl(
    private val productService: ProductService,
    private val orderService: OrderService,
    private val productSearchMapper: ProductSearchMapper
) : SharedSearchRepository {

    @Volatile
    private var selectedCategories: ArrayList<Long> = arrayListOf()

    @Volatile
    private var isSelectByPopularity: Boolean = false

    @Volatile
    private var isSelectByRating: Boolean = false

    @Volatile
    private var minCost: Float = 1f

    @Volatile
    private var maxCost: Float = 1000000f

    @Volatile
    private var minRating: Float = 0f

    @Volatile
    private var maxRating: Float = 10f

    @Volatile
    private var numberPage: Int = 0

    @Volatile
    private var searchText: String? = ""

    override suspend fun getCategories(): Result<List<CategoryWithCheck>> =
        withContext(Dispatchers.IO) {
            val response = productService.getPopularCategories()
            if (response.isSuccessful) {
                val categories = response.body()
                if (categories != null) {
                    val categoriesModel = categories.map {
                        CategoryWithCheck(
                            Category(id = it.id, name = it.name),
                            isSelected = false
                        )
                    }
                    return@withContext Result.Success(categoriesModel)
                }
            }
            return@withContext Result.Error()
        }

    override fun clearNumberPage() {
        numberPage = 0
    }

    override fun getMinPrice(): Float {
        return minCost
    }

    override fun getMaxPrice(): Float {
        return maxCost
    }

    override fun getMinRating(): Float {
        return minRating
    }

    override fun getMaxRating(): Float {
        return maxRating
    }

    override fun getIsSortByPopularity(): Boolean {
        return isSelectByPopularity
    }

    override fun getIsSortByRating(): Boolean {
        return isSelectByRating
    }

    override fun getSelectedCategories(): ArrayList<Long> {
        return selectedCategories
    }

    override suspend fun setIsSelectByPopularity(): Boolean {
        isSelectByPopularity = !isSelectByPopularity
        if (isSelectByPopularity) {
            isSelectByRating = false
        }
        searchText = null
        return isSelectByPopularity
    }

    override suspend fun setIsSelectByRating(): Boolean {
        isSelectByRating = !isSelectByRating
        if (isSelectByRating) {
            isSelectByPopularity = false
        }
        searchText = null
        return isSelectByRating
    }

    override suspend fun updateSelectedCategories(categoryWithCheck: CategoryWithCheck) {
        if (categoryWithCheck.isSelected) {
            if (selectedCategories.find { categoryWithCheck.category.id == it } == null) {
                selectedCategories.add(categoryWithCheck.category.id)
                searchText = null
            }
        } else {
            selectedCategories.removeIf { it == categoryWithCheck.category.id }
            searchText = null
        }
    }

    override suspend fun updateCostBoundaries(minCost: Float, maxCost: Float) {
        if (minCost <= maxCost) {
            this.minCost = minCost
            this.maxCost = maxCost
            searchText = null
        }
    }


    override suspend fun updateRatingBoundaries(minRating: Float, maxRating: Float) {
        if (minRating <= maxRating) {
            this.minRating = minRating
            this.maxRating = maxRating
            searchText = null
        }
    }

    override suspend fun setProductCount(productId: Long, count: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            val result = orderService.setProductCount(productId, count)
            if (result.isSuccessful) {
                return@withContext Result.Success()
            }
            return@withContext Result.Error()
        }

    override suspend fun searchProducts(word: String): Result<List<ProductSearchModel>> =
        withContext(Dispatchers.IO) {
            updateNumberPage(word)
            val categories =
                if (selectedCategories.size != 0) selectedCategories.joinToString(",") else null
            val response = productService.searchProducts(
                numberPage = numberPage,
                sizePage = PAGE_SIZE,
                word = word,
                sort = getSortingType(),
                minRating = minRating.toInt(),
                maxRating = maxRating.toInt(),
                minPrice = minCost.toInt(),
                maxPrice = maxCost.toInt(),
                categories = categories,
            )
            if (response.isSuccessful) {
                searchText = word
                val body = response.body()
                if (body != null) {
                    if (body.listOfProducts.isEmpty() && numberPage == 0) {
                        return@withContext Result.Error(SearchEmpty())
                    }
                    val models =
                        body.listOfProducts.map { productSearchMapper.mapFromResponseToModel(it) }
                    return@withContext Result.Success(models)
                }
            }
            return@withContext Result.Error()
        }

    private fun getSortingType(): String {
        return when {
            isSelectByPopularity -> SEARCH_BY_POPULARITY
            isSelectByRating -> SEARCH_BY_RATING
            else -> SEARCH_NOTHING
        }
    }

    private fun updateNumberPage(text: String) {
        if (searchText == null || text != searchText) {
            numberPage = 0
        } else {
            numberPage++
        }
    }
}