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
                    name.compose(timeoutMs = 1000, intervalMs = 199) {
                        or {
                            hasText(categoryName)
                        } thenContinue {
                            step("Выбрать категорию $categoryName") {
                                click()
                            }
                        }
                        or { }
                    }
                }
            }
        }
    }
}