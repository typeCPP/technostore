package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.product.ProductDetailScreen
import com.technostore.test.utils.TestData
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProductTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun productTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.PETROV_ACCESS_TOKEN,
            refreshToken = TestData.PETROV_REFRESH_TOKEN,
            email = TestData.PETROV_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)
        step("Ввести название товара") {
            MainScreen {
                searchField {
                    replaceText("TECNO")
                }
                closeSoftKeyboard()
            }
        }
        step("Нажать на товар") {
            MainScreen {
                Screen.idle(1000)
                searchResult {
                    firstChild<MainScreen.ProductItem> {
                        isVisible()
                        click()
                    }
                }
            }
        }
        ProductDetailScreen {
            step("Проверить, что отображается название товара") {
                name {
                    isVisible()
                }
            }
            step("Проверить, что отображается цена товара") {
                price {
                    isVisible()
                }
            }
            step("Проверить, что отображается категория товара") {
                category {
                    isVisible()
                }
            }
            step("Проверить, что отображается описание") {
                description {
                    isVisible()
                }
            }
            step("Проверить, что отображаются отзывы") {
                reviews {
                    scrollTo(0)
                    childAt<ProductDetailScreen.ReviewItem>(0) {
                        isVisible()
                    }
                    scrollTo(1)
                    childAt<ProductDetailScreen.ReviewItem>(1) {
                        isVisible()
                    }
                    scrollTo(2)
                    childAt<ProductDetailScreen.ReviewItem>(2) {
                        isVisible()
                    }
                }
            }
        }
    }
}