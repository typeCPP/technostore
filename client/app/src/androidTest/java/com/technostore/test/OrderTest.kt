package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.BottomNavMenuScreen
import com.technostore.screen.banner.ErrorScreen
import com.technostore.screen.shopping_cart.ShoppingCartScreen
import com.technostore.test.utils.TestData
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import com.technostore.feature_shopping_cart.R
import com.technostore.scenario.CheckOrderDetailScenario
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.order.OrderDetailScreen
import com.technostore.screen.order.OrdersScreen
import com.technostore.screen.product.ProductDetailScreen
import com.technostore.screen.profile.ProfileScreen
import io.github.kakaocup.kakao.screen.Screen

@HiltAndroidTest
class OrderTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun confirmOrderTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.IVANOVA_ACCESS_TOKEN,
            refreshToken = TestData.IVANOVA_REFRESH_TOKEN,
            email = TestData.IVANOVA_EMAIl
        )
        ActivityScenario.launch(LoginActivity::class.java)

        step("Открыть экран корзина") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    shoppingCart {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Проверить, что корзина не пустая, в ней отображаются товары") {
            ShoppingCartScreen {
                recyclerView {
                    isVisible()
                    childAt<ShoppingCartScreen.ProductItem>(0) {
                        isVisible()
                        name.isVisible()
                        price.isVisible()
                        image.isVisible()
                        minusButton.isVisible()
                        count.isVisible()
                        plusButton.isVisible()
                        rating.isVisible()
                    }
                }
            }
        }
        step("Нажать на кнопку Оформить заказ") {
            ShoppingCartScreen {
                submitButton {
                    isVisible()
                    click()
                }
            }
            step("Отображается экран корзина пустая") {
                ErrorScreen {
                    title {
                        isVisible()
                        hasText(R.string.shopping_cart_empty)
                    }
                    description {
                        isVisible()
                        hasText(R.string.shopping_cart_start_shopping)
                    }
                    button {
                        isVisible()
                        hasText(R.string.shopping_cart_empty_button)
                    }
                }
            }
        }
        step("Открыть экран профиль") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    personalAccount {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Нажать на кнопку история заказов") {
            ProfileScreen {
                orderHistoryButton {
                    isVisible()
                    click()
                }
            }
        }
        step("в истории заказов отображается оформленный заказ") {
            OrdersScreen {
                recyclerView {
                    isVisible()
                    childAt<OrdersScreen.OrderItem>(0) {
                        orderNumber.isVisible()
                    }
                }
            }
        }
    }

    @Test
    fun addProductToShoppingCartTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.ALENA_ACCESS_TOKEN,
            refreshToken = TestData.ALENA_REFRESH_TOKEN,
            email = TestData.ALENA_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)
        step("Нажать на товар") {
            Screen.idle(3000)
            MainScreen {
                popularRecyclerView {
                    firstChild<MainScreen.ProductItem> {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Нажать на кнопку “добавить в корзину") {
            ProductDetailScreen {
                shoppingCartButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Открыть экран корзина") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    shoppingCart {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Проверить, что корзина не пустая, в ней отображаeтся товары") {
            ShoppingCartScreen {
                recyclerView {
                    isVisible()
                    firstChild<ShoppingCartScreen.ProductItem> {
                        isVisible()
                        name.isVisible()
                        price.isVisible()
                        image.isVisible()
                        minusButton.isVisible()
                        count.isVisible()
                        plusButton.isVisible()
                        rating.isVisible()
                    }
                }
            }
        }
    }

    @Test
    fun ordersHistoryTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.PETROV_ACCESS_TOKEN,
            refreshToken = TestData.PETROV_REFRESH_TOKEN,
            email = TestData.PETROV_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)
        step("Открыть экран профиль") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    personalAccount {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Нажать на кнопку история заказов") {
            ProfileScreen {
                orderHistoryButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Отобразились все заказы пользователя") {
            OrdersScreen {
                recyclerView {
                    isVisible()
                    childAt<OrdersScreen.OrderItem>(0) {
                        isVisible()
                    }
                    childAt<OrdersScreen.OrderItem>(1) {
                        isVisible()
                    }
                }
            }
        }
        step("Проверить, что заказы соответсвуют ранее оформленным") {
            step("Проверить первый заказ") {
                scenario(
                    CheckOrderDetailScenario(
                        position = 0,
                        productName = "Ноутбук TECNO Megabook T1 T15TA Grey"
                    )
                )
            }
            step("Нажать кнопку назад") {
                OrderDetailScreen {
                    backButton {
                        isVisible()
                        click()
                    }
                }
            }
            step("Проверить второй заказ") {
                scenario(
                    CheckOrderDetailScenario(
                        position = 1,
                        productName = "Ноутбук HUAWEI MateBook D 14 i3 1115G4/8/256Gb DOS Space Grayy"
                    )
                )
            }
        }
    }
}