package com.technostore.feature_shopping_cart.shopping_cart.presentation

import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_shopping_cart.business.ShoppingCartRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ShoppingCartEffectHandlerTest : ShoppingCartBaseTest() {
    private val shoppingCartRepository = mockk<ShoppingCartRepository> {
        coEvery { getCurrentOrder() } returns Result.Success(order)
        coEvery { setProductCount(any(), any()) } returns Result.Success()
        coEvery { makeOrderCompleted(any()) } returns Result.Success()
    }
    private val effectHandler = ShoppingCartEffectHandler(
        shoppingCartRepository = shoppingCartRepository
    )

    /* Init */
    @Test
    fun `event init → start loading, get current order return success → set data`() = runTest {
        val event = ShoppingCartUiEvent.Init
        effectHandler.process(event, defaultState, store)
        val expectedEvent = ShoppingCartEvent.OrderDetailsLoaded(order)
        coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.StartLoading) }
        coVerify(exactly = 1) { shoppingCartRepository.getCurrentOrder() }
        coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.EndLoading) }
        coVerify(exactly = 1) { store.dispatch(expectedEvent) }
    }

    @Test
    fun `event init → start loading, get current order return success with emtpty body → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { getCurrentOrder() } returns Result.Success()
            }
            val event = ShoppingCartUiEvent.Init
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.StartLoading) }
            coVerify(exactly = 1) { shoppingCartRepository.getCurrentOrder() }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    @Test
    fun `event init → start loading, get current order return error → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { getCurrentOrder() } returns Result.Error()
            }
            val event = ShoppingCartUiEvent.Init
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.StartLoading) }
            coVerify(exactly = 1) { shoppingCartRepository.getCurrentOrder() }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    /* OnPlusClicked */
    @Test
    fun `event OnPlusClicked → update count, set product count return success → do nothing`() =
        runTest {
            val event = ShoppingCartUiEvent.OnPlusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.dispatch(
                    ShoppingCartEvent.UpdateCount(
                        productId = TestData.FIRST_PRODUCT_ID,
                        count = TestData.FIRST_PRODUCT_COUNT
                    )
                )
            }
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = TestData.FIRST_PRODUCT_COUNT
                )
            }
            coVerify(exactly = 0) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    @Test
    fun `event OnPlusClicked → update count, set product count return error → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { setProductCount(any(), any()) } returns Result.Error()
            }
            val event = ShoppingCartUiEvent.OnPlusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.dispatch(
                    ShoppingCartEvent.UpdateCount(
                        productId = TestData.FIRST_PRODUCT_ID,
                        count = TestData.FIRST_PRODUCT_COUNT
                    )
                )
            }
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = TestData.FIRST_PRODUCT_COUNT
                )
            }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    /* OnMinusClicked */
    @Test
    fun `event OnMinusClicked → update count, set product count return success → do nothing`() =
        runTest {
            val event = ShoppingCartUiEvent.OnMinusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.dispatch(
                    ShoppingCartEvent.UpdateCount(
                        productId = TestData.FIRST_PRODUCT_ID,
                        count = TestData.FIRST_PRODUCT_COUNT
                    )
                )
            }
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = TestData.FIRST_PRODUCT_COUNT
                )
            }
            coVerify(exactly = 0) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    @Test
    fun `event OnMinusClicked → update count, set product count return error → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { setProductCount(any(), any()) } returns Result.Error()
            }
            val event = ShoppingCartUiEvent.OnMinusClicked(
                productId = TestData.FIRST_PRODUCT_ID,
                count = TestData.FIRST_PRODUCT_COUNT
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                store.dispatch(
                    ShoppingCartEvent.UpdateCount(
                        productId = TestData.FIRST_PRODUCT_ID,
                        count = TestData.FIRST_PRODUCT_COUNT
                    )
                )
            }
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = TestData.FIRST_PRODUCT_COUNT
                )
            }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    /* OnRemoveClicked */
    @Test
    fun `event OnRemoveClicked → update count, set product count return success → do nothing`() =
        runTest {
            val event = ShoppingCartUiEvent.OnRemoveClicked(productId = TestData.FIRST_PRODUCT_ID)
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.RemoveItem(TestData.FIRST_PRODUCT_ID)) }
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = 0
                )
            }
            coVerify(exactly = 0) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    @Test
    fun `event OnRemoveClicked → update count, set product count return error → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { setProductCount(any(), any()) } returns Result.Error()
            }
            val event = ShoppingCartUiEvent.OnRemoveClicked(
                productId = TestData.FIRST_PRODUCT_ID
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) {
                shoppingCartRepository.setProductCount(
                    productId = TestData.FIRST_PRODUCT_ID,
                    count = 0
                )
            }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    /* OnSetOrdersClicked */
    @Test
    fun `event OnSetOrdersClicked → start loading, make order completed return success → stop loading, show success toast, complete order`() =
        runTest {
            val event = ShoppingCartUiEvent.OnSetOrdersClicked
            effectHandler.process(event, defaultState.copy(orderId = TestData.FIRST_ORDER_ID), store)
            coVerify(exactly = 1) { shoppingCartRepository.makeOrderCompleted(TestData.FIRST_ORDER_ID) }
            coVerify(exactly = 1) { store.dispatch(ShoppingCartEvent.OrderHasBeenPlaced) }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowSuccessToast) }
        }

    @Test
    fun `event OnSetOrdersClicked → start loading, make order completed return error → show error toast`() =
        runTest {
            shoppingCartRepository.apply {
                coEvery { makeOrderCompleted(any()) } returns Result.Error()
            }
            val event = ShoppingCartUiEvent.OnSetOrdersClicked
            effectHandler.process(event, defaultState.copy(orderId = TestData.FIRST_ORDER_ID), store)
            coVerify(exactly = 1) { shoppingCartRepository.makeOrderCompleted(TestData.FIRST_ORDER_ID) }
            coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.ShowErrorToast) }
        }

    /* OnStartShoppingClicked */
    @Test
    fun `event OnStartShoppingClicked → open main page`() = runTest {
        val event = ShoppingCartUiEvent.OnStartShoppingClicked
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(ShoppingCartNews.OpenMainPage) }
    }
}