package com.technostore.screen.banner

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KTextView
import com.technostore.core.R
import io.github.kakaocup.kakao.text.KButton

object ErrorScreen : KScreen<ErrorScreen>() {
    override val layoutId: Int = R.layout.error_fragment
    override val viewClass: Class<*>? = null

    val title = KTextView { withId(R.id.title) }
    val description = KTextView { withId(R.id.description) }
    val button = KButton { withId(R.id.button) }
}