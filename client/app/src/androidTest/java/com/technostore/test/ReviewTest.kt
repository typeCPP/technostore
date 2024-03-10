package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.product.ProductDetailScreen
import com.technostore.screen.review.ReviewListScreen
import com.technostore.test.utils.TestData
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ReviewTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun allReviewsTest() = run {
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
                searchResult {
                    firstChild<MainScreen.ProductItem> {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Нажать на кнопку “Посмотреть все” у отзыва") {
            ProductDetailScreen {
                allReviewsButton {
                    isVisible()
                    click()
                }
            }
        }
        ReviewListScreen {
            step("Открылся экран со всеми отзывами") {
                title.isVisible()
            }
            step("Нажать на кнопку 7-10") {
                positiveReviewsButton {
                    isVisible()
                    click()
                }
            }
            step("Отображаются отзывы только с оценками 7-10") {
                ReviewListScreen {
                    recyclerView.firstChild<ReviewListScreen.ReviewItem> {
                        isVisible()
                        rating {
                            isVisible()
                            hasText("10")
                        }
                    }
                }
            }
            step("Нажать на кнопку 4-6") {
                neutralReviewsButton {
                    isVisible()
                    click()
                }
            }
            step("Отображаются отзывы только с оценками 4-6") {
                ReviewListScreen {
                    recyclerView.firstChild<ReviewListScreen.ReviewItem> {
                        isVisible()
                        rating {
                            isVisible()
                            hasText("6")
                        }
                    }
                }
            }
            step("Нажать на кнопку 1-3") {
                negativeReviewsButton {
                    isVisible()
                    click()
                }
            }
            step("Отображаются отзывы только с оценками 1-3") {
                ReviewListScreen {
                    recyclerView.firstChild<ReviewListScreen.ReviewItem> {
                        isVisible()
                        rating {
                            isVisible()
                            hasText("2")
                        }
                    }
                }
            }
        }
    }
}