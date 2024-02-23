package com.technostore.screen.sign_in

import com.kaspersky.kaspresso.screens.KScreen
import com.technostore.feature_login.sign_in.SignInFragment
import com.technostore.feature_login.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object SignInScreen : KScreen<SignInScreen>() {
    override val layoutId: Int = R.layout.sign_in_fragment
    override val viewClass: Class<*> = SignInFragment::class.java

    val email = KEditText { withId(R.id.et_email) }
    val password = KEditText { withId(R.id.et_password) }
    val signInButton = KButton { withId(R.id.tv_button_sign_in) }
}