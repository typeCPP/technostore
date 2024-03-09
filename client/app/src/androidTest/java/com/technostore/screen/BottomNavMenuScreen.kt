package com.technostore.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.text.KButton
import com.technostore.base.R

object BottomNavMenuScreen : KScreen<BottomNavMenuScreen>() {
    override val layoutId: Int = R.menu.menu
    override val viewClass: Class<*>? = null

    val home = KButton { withId(R.id.home) }

    // Элемент перехода к Операциям
    val search = KButton { withId(R.id.search) }

    // Элемент перехода к Аналитике
    val shoppingCart = KButton { withId(R.id.shopping_cart) }

    // Элемент перехода к Чату
    val personalAccount = KButton { withId(R.id.personal_account) }
}
