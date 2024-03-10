package com.technostore.screen.product

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_product.R
import com.technostore.feature_product.product.ProductDetailFragment
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ProductDetailScreen : KScreen<ProductDetailScreen>() {
    override val layoutId: Int = R.layout.product_fragment
    override val viewClass: Class<*> = ProductDetailFragment::class.java

    val shoppingCartButton = KImageView { withId(R.id.iv_shopping_cart) }

    val allReviewsButton = KTextView { withId(R.id.tv_review_more) }

    val name = KTextView { withId(R.id.product_name) }
    val price = KTextView { withId(R.id.price) }
    val category = KTextView { withId(R.id.category) }
    val description = KTextView { withId(R.id.tv_description) }
    val reviews = KRecyclerView({
        withId(R.id.rv_review)
    },
        itemTypeBuilder = {
            itemType(ProductDetailScreen::ReviewItem)
        }
    )

    class ReviewItem(parent: Matcher<View>) : KRecyclerItem<ReviewItem>(parent) {
        val userName = KTextView(parent) { withId(R.id.tv_user_name) }
        val text = KTextView(parent) { withId(R.id.tv_review) }
    }
}