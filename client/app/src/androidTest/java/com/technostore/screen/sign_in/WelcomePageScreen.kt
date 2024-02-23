package com.technostore.screen.sign_in

import com.technostore.feature_login.R
import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_login.welcome_page.WelcomePageFragment
import io.github.kakaocup.kakao.text.KButton

object WelcomePageScreen : KScreen<WelcomePageScreen>() {
    override val layoutId: Int get() = R.layout.welcome_page_fragment
    override val viewClass: Class<*> = WelcomePageFragment::class.java

    val nextButton = KButton { withIndex(0) { withId(R.id.next_button) } }
}