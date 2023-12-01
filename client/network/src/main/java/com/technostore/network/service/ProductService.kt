package com.technostore.network.service

import com.technostore.network.model.product.response.CategoryResponse
import com.technostore.network.model.product.response.ProductDetailResponse
import com.technostore.network.utils.URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {
    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/product/popular-categories")
    suspend fun getPopularCategories(): Response<List<CategoryResponse>>

    @GET("${URL.PRODUCT_SERVICE_BASE_URL}/product/{id}")
    suspend fun getProductById(@Path("id") id: Long): Response<ProductDetailResponse>
}