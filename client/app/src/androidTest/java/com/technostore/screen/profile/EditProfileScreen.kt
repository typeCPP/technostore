package com.technostore.screen.profile

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_profile.R
import com.technostore.feature_profile.edit_profile.EditProfileFragment
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView

object EditProfileScreen : KScreen<EditProfileScreen>() {
    override val layoutId: Int = R.layout.edit_profile_page_fragment
    override val viewClass: Class<*> = EditProfileFragment::class.java

    val name = KEditText { withId(R.id.et_name) }
    val lastName = KEditText { withId(R.id.et_nickname) }
    val saveButton = KButton { withId(R.id.tv_edit_profile_button_next) }

}