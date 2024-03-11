package com.technostore.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton
import com.technostore.base.R

object BottomNavMenuScreen : KScreen<BottomNavMenuScreen>() {
    override val layoutId: Int = R.menu.menu
    override val viewClass: Class<*>? = null

    val home = KButton { withId(R.id.home) }

    val search = KButton { withId(R.id.search) }

    val shoppingCart = KButton { withId(R.id.shopping_cart) }

    val personalAccount = KButton { withId(R.id.personal_account) }
}
