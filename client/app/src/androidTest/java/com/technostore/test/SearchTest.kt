package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.scenario.CheckProductSearchScenario
import com.technostore.screen.banner.ErrorScreen
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.search.FilterScreen
import com.technostore.test.utils.TestData
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Rule
import org.junit.Test
import com.technostore.shared_search.R as SharedSearchR


@HiltAndroidTest
class SearchTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun searchProductTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.PETROV_ACCESS_TOKEN,
            refreshToken = TestData.PETROV_REFRESH_TOKEN,
            email = TestData.PETROV_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)
        step("Нажать на кнопку поиска") {
            Screen.idle(3000)
            MainScreen {
                searchField {
                    isVisible()
                    click()
                }
            }
        }
        step("Ввести название, которое не существует") {
            MainScreen {
                searchField {
                    typeText("skdkdkdkkfkfkfkf")
                }
                closeSoftKeyboard()
            }
        }
        step("Отображается экран Ничего не найдено") {
            ErrorScreen {
                title {
                    isVisible()
                    hasText(SharedSearchR.string.search_not_found)
                }
            }
        }
    }

    @Test
    fun searchByPopularityAndCategoryTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.PETROV_ACCESS_TOKEN,
            refreshToken = TestData.PETROV_REFRESH_TOKEN,
            email = TestData.PETROV_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)
        step("Нажать на кнопку фильтра") {
            Screen.idle(1000)
            MainScreen {
                filterButton {
                    isVisible()
                    click()
                }
            }
        }
        FilterScreen {
            step("Выбрать категорию ноутбуки") {
                categories {
                    isVisible()
                    childAt<FilterScreen.CategoryItem>(8) {
                        orderNumber {
                            isVisible()
                            click()
                        }
                    }
                }
            }
            step("Нажать искать") {
                submitButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Отображаются ноутбуки") {
            step("Проверить первый товар") {
                CheckProductSearchScenario(
                    position = 0,
                    productName = "Ноутбук TECNO Megabook T1 T15TA Grey"
                )
            }
            step("Проверить второй товар") {
                CheckProductSearchScenario(
                    position = 1,
                    productName = "Ноутбук HUAWEI MateBook D 14 i3 1115G4/8/256Gb DOS Space Grayy"
                )
            }
            step("Проверить третий товар") {
                CheckProductSearchScenario(
                    position = 2,
                    productName = "Ноутбук игровой Thunderobot 911 Air XS D"
                )
            }
            step("Проверить четвертый товар") {
                CheckProductSearchScenario(
                    position = 3,
                    productName = "Ноутбук Digma DN16R3-8CXW01"
                )
            }
            step("Проверить пятый товар") {
                CheckProductSearchScenario(
                    position = 4,
                    productName = "Ноутбук Apple MacBook Air 13 M1/8/256 Space Gray (MGN63)"
                )
            }
        }
    }
}