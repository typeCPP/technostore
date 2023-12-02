package com.technostore.network.service

import com.technostore.network.model.product.response.CategoryResponse
import com.technostore.network.model.product.response.ProductDetailResponse
import com.technostore.network.model.product.response.ProductSearchResultResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/product/popular-categories")
    suspend fun getPopularCategories(): Response<List<CategoryResponse>>

    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/product/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<ProductDetailResponse>

    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/product/search")
    suspend fun searchProducts(
        @Query("numberPage") numberPage: Int,
        @Query("sizePage") sizePage: Int,
        @Query("word") word: String?,
        @Query("sort") sort: String?,
        @Query("minRating") minRating: Int?,
        @Query("maxRating") maxRating: Int?,
        @Query("categories") categories: String?,
        @Query("minPrice") minPrice: Int?,
        @Query("maxPrice") maxPrice: Int?,
    ): Response<ProductSearchResultResponse>
}