package com.technostore.screen.order

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_order.R
import com.technostore.feature_order.orders.OrdersFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher


object OrdersScreen : KScreen<OrdersScreen>() {
    override val layoutId: Int = R.layout.orders_fragment
    override val viewClass: Class<*> = OrdersFragment::class.java

    val recyclerView = KRecyclerView({
        withId(R.id.orders_list)
    },
        itemTypeBuilder = {
            itemType(OrdersScreen::OrderItem)
        }
    )

    class OrderItem(parent: Matcher<View>) : KRecyclerItem<OrderItem>(parent) {
        val orderNumber = KTextView(parent) { withId(R.id.order_number) }
    }
}