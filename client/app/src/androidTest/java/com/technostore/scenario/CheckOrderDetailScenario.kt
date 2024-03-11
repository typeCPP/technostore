package com.technostore.scenario

import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext
import com.technostore.screen.order.OrderDetailScreen
import com.technostore.screen.order.OrdersScreen

class CheckOrderDetailScenario(
    private val position: Int,
    private val productName: String
) : Scenario() {
    override val steps: TestContext<Unit>.() -> Unit = {
        OrdersScreen {
            recyclerView {
                childAt<OrdersScreen.OrderItem>(position) {
                    click()
                }
            }
        }
        OrderDetailScreen {
            recyclerView {
                firstChild<OrderDetailScreen.ProductItem> {
                    name {
                        isVisible()
                        hasText(productName)
                    }
                }
            }
        }
    }
}
