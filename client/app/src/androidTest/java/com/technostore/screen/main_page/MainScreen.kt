package com.technostore.screen.main_page

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_main_page.R
import com.technostore.feature_main_page.main_page.MainPageFragment
import io.github.kakaocup.kakao.text.KTextView

object MainScreen : KScreen<MainScreen>() {
    override val layoutId: Int = R.layout.main_fragment
    override val viewClass: Class<*> = MainPageFragment::class.java

    val tvPopular = KTextView { withId(R.id.tv_popular) }

}