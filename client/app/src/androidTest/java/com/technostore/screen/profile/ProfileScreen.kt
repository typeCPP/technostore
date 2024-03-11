package com.technostore.screen.profile

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_profile.R
import com.technostore.feature_profile.profile.ProfileFragment
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object ProfileScreen : KScreen<ProfileScreen>() {
    override val layoutId: Int = R.layout.profile_page_fragment
    override val viewClass: Class<*> = ProfileFragment::class.java

    val name = KTextView { withId(R.id.tv_name) }
    val orderHistoryButton = KTextView { withId(R.id.tv_order_history) }
    val changePasswordButton = KTextView { withId(R.id.tv_change_password) }
    val logoutButton = KTextView { withId(R.id.tv_logout) }
    val changeProfileButton = KImageView { withId(R.id.iv_pencil) }
}