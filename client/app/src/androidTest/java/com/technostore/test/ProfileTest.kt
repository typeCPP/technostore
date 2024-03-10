package com.technostore.test

import androidx.test.core.app.ActivityScenario
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.technostore.base.activity.LoginActivity
import com.technostore.screen.BottomNavMenuScreen
import com.technostore.screen.main_page.MainScreen
import com.technostore.screen.profile.ChangePasswordScreen
import com.technostore.screen.profile.EditProfileScreen
import com.technostore.screen.profile.ProfileScreen
import com.technostore.screen.sign_in.SignInScreen
import com.technostore.test.utils.TestData
import com.technostore.test.utils.TestExt
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.github.kakaocup.kakao.screen.Screen
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProfileTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    fun changePasswordTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.VLAD_ACCESS_TOKEN,
            refreshToken = TestData.VLAD_REFRESH_TOKEN,
            email = TestData.VLAD_EMAIL
        )
        ActivityScenario.launch(LoginActivity::class.java)

        step("Открыть экран профиль") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    personalAccount {
                        isVisible()
                        click()
                    }
                }
            }
        }
        step("Нажать на кнопку изменить пароль") {
            ProfileScreen {
                changePasswordButton {
                    isVisible()
                    click()
                }
            }
        }
        ChangePasswordScreen {
            step("Ввести правильный старый пароль") {
                oldPassword {
                    isVisible()
                    click()
                    typeText(TestData.DEFAULT_PASSWORD)
                }
                closeSoftKeyboard()
            }
            step("Ввести валидный новый пароль") {
                firstNewPassword {
                    isVisible()
                    click()
                    typeText(TestData.VLAD_NEW_PASSWORD)
                }
                closeSoftKeyboard()
            }
            step("Ввести такой же новый пароль") {
                secondNewPassword {
                    isVisible()
                    click()
                    typeText(TestData.VLAD_NEW_PASSWORD)
                }
                closeSoftKeyboard()
            }
            step("Нажать сохранить") {
                saveButton {
                    isVisible()
                    click()
                }
            }
        }
        ProfileScreen {
            step("Открылся экран профиль") {
                changePasswordButton {
                    isVisible()
                }
            }
            step("Нажать на кнопку выйти из аккаунта") {
                logoutButton {
                    isVisible()
                    click()
                }
            }
        }
        SignInScreen {
            step("Ввести e-mail от учетной записи") {
                email {
                    isVisible()
                    click()
                    typeText(TestData.VLAD_EMAIL)
                }
                closeSoftKeyboard()
            }
            step("Ввести новый пароль") {
                password {
                    isVisible()
                    click()
                    typeText(TestData.VLAD_NEW_PASSWORD)
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
                tvPopular {
                    isVisible()
                }
            }
        }
    }

    @Test
    fun changeNameTest() = run {
        TestExt.setupActiveUserClass(
            accessToken = TestData.IVANOVA_ACCESS_TOKEN,
            refreshToken = TestData.IVANOVA_REFRESH_TOKEN,
            email = TestData.IVANOVA_EMAIl
        )
        ActivityScenario.launch(LoginActivity::class.java)

        step("Открыть экран профиль") {
            flakySafely(5000) {
                BottomNavMenuScreen {
                    personalAccount {
                        isVisible()
                        click()
                    }
                }
            }
        }
        ProfileScreen {
            step("Проверить, что имя изменилось") {
                name {
                    isVisible()
                    hasText("${TestData.IVANOVA_NAME} ${TestData.IVANOVA_LAST_NAME}")
                }
            }
            step("Нажать на кнопку изменения профиля") {
                changeProfileButton {
                    isVisible()
                    click()
                }

            }
        }
        EditProfileScreen {
            step("Ввести новое имя") {
                name {
                    isVisible()
                    click()
                    replaceText(TestData.IVANOVA_NEW_NAME)
                }
                closeSoftKeyboard()
            }
            step("Ввести новую фамилию") {
                lastName {
                    isVisible()
                    click()
                    replaceText(TestData.IVANOVA_NEW_LAST_NAME)
                }
                closeSoftKeyboard()
            }
            step("Нажать сохранить") {
                Screen.idle(1000)
                flakySafely(5000) {
                    saveButton {
                        isDisplayed()
                        click()
                    }
                }
            }
        }
        ProfileScreen {
            step("Открылся экран профиль") {
                changePasswordButton {
                    isVisible()
                }
            }
            step("Проверить, что имя изменилось") {
                name {
                    isVisible()
                    hasText("${TestData.IVANOVA_NEW_NAME} ${TestData.IVANOVA_NEW_LAST_NAME}")
                }
            }
        }
    }
}