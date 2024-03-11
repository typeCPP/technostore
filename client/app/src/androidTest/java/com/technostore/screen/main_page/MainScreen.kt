package com.technostore.screen.main_page

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_main_page.R
import com.technostore.feature_main_page.main_page.MainPageFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import com.technostore.core.R as CoreR
import com.technostore.shared_search.R as SharedSearchR

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.main_fragment
    override val viewClass: Class<*> = MainPageFragment::class.java

    val tvPopular = KTextView { withId(R.id.tv_popular) }
    val popularRecyclerView = KRecyclerView({
        withId(R.id.rv_popular)
    },
        itemTypeBuilder = {
            itemType(MainScreen::ProductItem)
        }
    )

    val searchResult = KRecyclerView({
        withId(R.id.products_list)
    },
        itemTypeBuilder = {
            itemType(MainScreen::ProductItem)
        }
    )

    val searchField = KEditText { withId(SharedSearchR.id.et_search) }
    val filterButton = KImageView { withId(SharedSearchR.id.iv_filter_button) }

    class ProductItem(parent: Matcher<View>) : KRecyclerItem<ProductItem>(parent) {
        val image = KImageView(parent) { withId(CoreR.id.iv_picture) }
        val name = KTextView(parent) { withId(CoreR.id.name) }
        val rating = KTextView(parent) { withId(CoreR.id.rating) }
        val price = KTextView(parent) { withId(CoreR.id.price) }
    }
}