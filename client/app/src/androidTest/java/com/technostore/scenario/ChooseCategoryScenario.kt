package com.technostore.scenario

import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.technostore.screen.search.FilterScreen

class ChooseCategoryScenario(private val categoryName: String) : Scenario() {
    override val steps: TestContext<Unit>.() -> Unit = {
        FilterScreen {
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 0))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 1))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 2))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 3))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 4))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 5))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 6))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 7))
            scenario(CheckCategoryScenario(categoryName=categoryName, position = 8))
        }
    }
}