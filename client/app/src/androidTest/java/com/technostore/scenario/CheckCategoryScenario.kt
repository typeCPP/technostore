package com.technostore.scenario

import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.technostore.screen.search.FilterScreen

class CheckCategoryScenario(
    private val categoryName: String,
    private val position: Int
) : Scenario() {
    override val steps: TestContext<Unit>.() -> Unit = {
        FilterScreen {
            categories {
                childAt<FilterScreen.CategoryItem>(position) {
                    compose {
                        or {
                            name.hasText(categoryName)
                        } thenContinue {
                            step("Выбрать категорию $categoryName") {
                                flakySafely {
                                    click()
                                }
                            }
                        }
                        or {  }
                    }
                }
            }
        }
    }
}