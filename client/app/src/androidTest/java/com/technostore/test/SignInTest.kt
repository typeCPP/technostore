package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.sign_in.SignInScreen
import com.technostore.screen.sign_in.WelcomePageScreen
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class SignInTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun signInTest() {
        TestExt.setupClass(isActive = false, isOnBoardingShown = false)
        ActivityScenario.launch(LoginActivity::class.java)
        WelcomePageScreen {
            nextButton {
                isVisible()
                click()
            }
        }
        SignInScreen {
            email {
                isVisible()
                click()
                typeText("danil@yandex.ru")
            }
            closeSoftKeyboard()
            password {
                isVisible()
                click()
                typeText("123456")
            }
            closeSoftKeyboard()
            signInButton {
                isVisible()
                click()
            }
        }
        MainScreen {
            Screen.idle(5000)
            tvPopular {
                isVisible()
            }
        }
    }
}