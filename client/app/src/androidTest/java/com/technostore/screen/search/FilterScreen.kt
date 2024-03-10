package com.technostore.screen.search

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView
import com.technostore.shared_search.R
import com.technostore.shared_search.filter.FilterFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import org.hamcrest.Matcher

object FilterScreen : KScreen<FilterScreen>() {
    override val layoutId: Int = R.layout.filter_fragment
    override val viewClass: Class<*> = FilterFragment::class.java

    val ratingButton = KTextView { withId(R.id.tv_sort_rating) }
    val categories = KRecyclerView({
        withId(R.id.rv_category)
    },
        itemTypeBuilder = {
            itemType(FilterScreen::CategoryItem)
        }
    )
    val submitButton=KTextView{withId(R.id.tv_search)}

    class CategoryItem(parent: Matcher<View>) : KRecyclerItem<CategoryItem>(parent) {
        val name = KTextView { withId(R.id.tv_name) }
    }
}