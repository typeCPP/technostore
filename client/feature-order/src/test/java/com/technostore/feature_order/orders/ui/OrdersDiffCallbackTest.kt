package com.technostore.feature_order.orders.ui

import org.junit.Assert.assertTrue
import org.junit.Test

class OrdersDiffCallbackTest {
   private val diffCallback = OrdersDiffCallback()

    /* areItemsTheSame */
    @Test
    fun `is one item → areItemsTheSame return true`() {
        val result = diffCallback.areItemsTheSame(1L, 1L)
        assertTrue(result)
    }

    @Test
    fun `is different items → areItemsTheSame return false`() {
        val result = diffCallback.areItemsTheSame(1L, 2L)
        assertTrue(!result)
    }

    /* areContentsTheSame */
    @Test
    fun `is one item → areContentsTheSame return true`() {
        val result = diffCallback.areContentsTheSame(1L, 1L)
        assertTrue(result)
    }

    @Test
    fun `is different items → areContentsTheSame return false`() {
        val result = diffCallback.areContentsTheSame(1L, 2L)
        assertTrue(!result)
    }
}