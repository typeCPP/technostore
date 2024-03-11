package com.technostore.screen.profile

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_profile.R
import com.technostore.feature_profile.change_password.ChangePasswordFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KTextView

object ChangePasswordScreen : KScreen<ChangePasswordScreen>() {
    override val layoutId: Int = R.layout.password_change_page_fragment
    override val viewClass: Class<*> = ChangePasswordFragment::class.java

    val oldPassword = KEditText { withId(R.id.et_old_password) }
    val firstNewPassword = KEditText { withId(R.id.et_enter_new_password) }
    val secondNewPassword = KEditText { withId(R.id.et_repeat_new_password) }
    val saveButton = KTextView { withId(R.id.tv_button_next) }
}