package com.technostore.screen.product

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_product.R
import com.technostore.feature_product.product.ProductDetailFragment
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object ProductDetailScreen : KScreen<ProductDetailScreen>() {
    override val layoutId: Int = R.layout.product_fragment
    override val viewClass: Class<*> = ProductDetailFragment::class.java

    val shoppingCartButton = KImageView { withId(R.id.iv_shopping_cart) }

    val allReviewsButton = KTextView { withId(R.id.tv_review_more) }
}