package com.technostore.screen.shopping_cart

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_shopping_cart.R
import com.technostore.core.R as CoreR
import com.technostore.feature_shopping_cart.shopping_cart.ShoppingCartFragment
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ShoppingCartScreen : KScreen<ShoppingCartScreen>() {
    override val layoutId: Int = R.layout.shopping_cart_fragment
    override val viewClass: Class<*> = ShoppingCartFragment::class.java

    val recyclerView = KRecyclerView({
        withId(R.id.products_list)
    },
        itemTypeBuilder = {
            itemType(ShoppingCartScreen::ProductItem)
        }
    )
    val submitButton = KImageView { withId(R.id.set_order) }

    class ProductItem(parent: Matcher<View>) : KRecyclerItem<ProductItem>(parent) {

        val image = KImageView(parent) { withId(CoreR.id.iv_picture) }
        val minusButton = KImageView(parent) { withId(CoreR.id.ic_minus) }
        val count = KTextView(parent) { withId(CoreR.id.count) }
        val plusButton = KImageView(parent) { withId(CoreR.id.ic_plus) }
        val name = KTextView(parent) { withId(CoreR.id.name) }
        val rating = KTextView(parent) { withId(CoreR.id.rating) }
        val price = KTextView(parent) { withId(CoreR.id.price) }
    }
}