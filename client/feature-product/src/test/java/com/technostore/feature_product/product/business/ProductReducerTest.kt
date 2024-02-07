package com.technostore.feature_product.product.business

import com.technostore.common_test.TestData
import com.technostore.feature_product.product.presentation.ProductEvent
import com.technostore.feature_product.product.presentation.ProductReducer
import com.technostore.feature_product.product.presentation.ProductState
import com.technostore.feature_product.product.presentation.ProductUiEvent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductReducerTest : ProductBaseTest() {
    private val reducer = ProductReducer()
    private val defaultState = ProductState()

    /* StartLoading */
    @Test
    fun `event StartLoading → set isLoading = true`() {
        val event = ProductEvent.StartLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = false), event)
        assertTrue(newState.isLoading)
    }

    /* EndLoading */
    @Test
    fun `event EndLoading → set isLoading = false`() {
        val event = ProductEvent.EndLoading
        val newState = reducer.reduce(defaultState.copy(isLoading = true), event)
        assertTrue(!newState.isLoading)
    }

    /* OnDataLoaded */
    @Test
    fun `event OnDataLoaded → update product, user rating`() {
        val event = ProductEvent.OnDataLoaded(productDetail = productDetailModel)
        val newState = reducer.reduce(defaultState.copy(productDetail = null), event)
        assertTrue(newState.productDetail == productDetailModel && newState.userRating == productDetailModel.userRating)
    }

    /* UpdateRating */
    @Test
    fun `event UpdateRating → product is not empty, review text is not empty → update rating, product detail`() {
        val event = ProductEvent.UpdateRating(
            newRating = TestData.USER_REVIEW_RATE,
            text = TestData.USER_REVIEW_TEXT
        )
        val newState = reducer.reduce(defaultState.copy(productDetail = productDetailModel), event)
        assertTrue(
            newState.productDetail == productDetailModel.copy(userRating = TestData.USER_REVIEW_RATE) &&
                    newState.userRating == TestData.USER_REVIEW_RATE &&
                    newState.userReviewText == TestData.USER_REVIEW_TEXT
        )
    }

    @Test
    fun `event UpdateRating → product is not empty, review text is empty → update rating, product detail`() {
        val event = ProductEvent.UpdateRating(
            newRating = TestData.USER_REVIEW_RATE,
            text = null
        )
        val newState = reducer.reduce(defaultState.copy(productDetail = productDetailModel), event)
        assertTrue(
            newState.productDetail == productDetailModel.copy(userRating = TestData.USER_REVIEW_RATE) &&
                    newState.userRating == TestData.USER_REVIEW_RATE &&
                    newState.userReviewText == null
        )
    }

    @Test
    fun `event UpdateRating → product is empty → does not update rating`() {
        val event = ProductEvent.UpdateRating(
            newRating = TestData.USER_REVIEW_RATE,
            text = null
        )
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }

    /* UpdateInCartCount */
    @Test
    fun `event UpdateInCartCount → product is not empty → update cart count`() {
        val currentState = defaultState.copy(productDetail = productDetailModel)
        val newCount = currentState.productDetail!!.inCartCount + 1
        val event = ProductEvent.UpdateInCartCount(newCount)
        val newState = reducer.reduce(currentState, event)
        val newProductDetail = newState.productDetail
        assertTrue(newProductDetail != null && newProductDetail.inCartCount == newCount)
    }

    @Test
    fun `event UpdateInCartCount → product is empty → does not update count`() {
        val event = ProductEvent.UpdateInCartCount(
            count = 1000
        )
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }

    /* OnReviewLoaded */
    @Test
    fun `event OnReviewLoaded → test is not empty → set review text`() {
        val event = ProductEvent.OnReviewLoaded(TestData.USER_REVIEW_TEXT)
        val newState = reducer.reduce(defaultState, event)
        assertEquals(newState.userReviewText, TestData.USER_REVIEW_TEXT)
    }

    @Test
    fun `event OnReviewLoaded → test is empty → set review text`() {
        val event = ProductEvent.OnReviewLoaded(null)
        val newState = reducer.reduce(defaultState, event)
        assertEquals(newState.userReviewText, null)
    }

    /* UpdateReviews */
    @Test
    fun `event UpdateReviews → product is not empty → update reviews`() {
        val reviews = listOf(reviewModel)
        val event = ProductEvent.UpdateReviews(reviews)
        val currentState = defaultState.copy(
            productDetail = productDetailModel.copy(reviews = emptyList())
        )
        val newState = reducer.reduce(currentState, event)
        val newProductDetail = newState.productDetail
        assertTrue(newProductDetail == productDetailModel.copy(reviews = reviews))
    }

    @Test
    fun `event UpdateReviews → product is empty → does not update reviews`() {
        val reviews = listOf(reviewModel)
        val event = ProductEvent.UpdateReviews(reviews)
        val currentState = defaultState.copy(
            productDetail = null
        )
        val newState = reducer.reduce(currentState, event)
        assertTrue(newState == currentState)
    }

    @Test
    fun `ui event → return current state`() {
        val event = ProductUiEvent.OnBackClicked
        val newState = reducer.reduce(defaultState, event)
        assertTrue(newState == defaultState)
    }
}