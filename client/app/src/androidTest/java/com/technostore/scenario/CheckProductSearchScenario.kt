package com.technostore.scenario

import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.technostore.screen.main_page.MainScreen

class CheckProductSearchScenario(
    private val position: Int,
    private val productName: String
) : Scenario() {
    override val steps: TestContext<Unit>.() -> Unit = {
        MainScreen {
            searchResult {
                scrollTo(position)
                childAt<MainScreen.ProductItem>(position) {
                    name {
                        isVisible()
                        hasText(productName)
                    }
                }
            }
        }
    }
}