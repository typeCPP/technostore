package com.technostore.screen.order

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_order.order_detail.OrderDetailFragment
import com.technostore.feature_order.R
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import com.technostore.core.R as CoreR

object OrderDetailScreen : KScreen<OrderDetailScreen>() {
    override val layoutId: Int = R.layout.order_details_fragment
    override val viewClass: Class<*> = OrderDetailFragment::class.java

    val recyclerView = KRecyclerView({
        withId(R.id.products_list)
    },
        itemTypeBuilder = {
            itemType(OrderDetailScreen::ProductItem)
        }
    )

    val backButton = KImageView { withId(R.id.iv_back_button) }

    class ProductItem(parent: Matcher<View>) : KRecyclerItem<ProductItem>(parent) {
        val image = KImageView(parent) { withId(CoreR.id.iv_picture) }
        val name = KTextView(parent) { withId(CoreR.id.name) }
        val rating = KTextView(parent) { withId(CoreR.id.rating) }
        val price = KTextView(parent) { withId(CoreR.id.price) }
    }
}