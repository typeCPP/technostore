package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.sign_in.SignInScreen
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
    fun signInTest() = run {
        TestExt.setupClass(isActive = false)
        ActivityScenario.launch(LoginActivity::class.java)
        SignInScreen {
            step("Проверить, что открылся экран Логина") {
                signInTitle.isVisible()
            }
            step("Ввести правильный e-mail от учетной записи") {
                email {
                    isVisible()
                    click()
                    typeText("danil@yandex.ru")
                }
                closeSoftKeyboard()
            }
            step("Ввести правильный пароль от учетной записи") {
                password {
                    isVisible()
                    click()
                    typeText("123456")
                }
                closeSoftKeyboard()
            }
            step("Нажать на кнопку войти") {
                signInButton {
                    isVisible()
                    click()
                }
            }
        }
        step("Открылась главная страница") {
            MainScreen {
                Screen.idle(5000)
                tvPopular {
                    isVisible()
                }
            }
        }
    }
}