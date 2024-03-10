package com.technostore.screen.review

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_review.R
import com.technostore.feature_review.review_list.ReviewListFragment
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

object ReviewListScreen : KScreen<ReviewListScreen>() {
    override val layoutId: Int = R.layout.review_list_fragment
    override val viewClass: Class<*> = ReviewListFragment::class.java

    val title = KTextView { withId(R.id.tv_title) }
    val allReviewsButton = KTextView { withId(R.id.all_reviews_button) }
    val positiveReviewsButton = KTextView { withId(R.id.tv_positive_reviews) }
    val neutralReviewsButton = KTextView { withId(R.id.tv_neutral_reviews) }
    val negativeReviewsButton = KTextView { withId(R.id.tv_negative_reviews) }
    val recyclerView = KRecyclerView({
        withId(R.id.rv_reviews)
    },
        itemTypeBuilder = {
            itemType(ReviewListScreen::ReviewItem)
        }
    )

    class ReviewItem(parent: Matcher<View>) : KRecyclerItem<ReviewItem>(parent) {
        val userName = KTextView(parent) { withId(R.id.tv_user_name) }
        val rating = KTextView(parent) { withId(R.id.tv_rating) }
        val text = KTextView(parent) { withId(R.id.tvReviewText) }
        val date = KTextView(parent) { withId(R.id.tv_date) }
    }
}