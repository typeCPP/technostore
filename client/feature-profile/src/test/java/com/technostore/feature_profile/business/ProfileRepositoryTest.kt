package com.technostore.feature_profile.business

import com.github.tomakehurst.wiremock.client.WireMock
import com.technostore.app_store.store.AppStore
import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.SessionServiceMock
import com.technostore.common_test.mock.UserServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_profile.business.error.ChangePasswordError
import com.technostore.feature_profile.business.model.ProfileModel
import com.technostore.network.utils.URL
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()

    private val appStore = mockk<AppStore>(relaxed = true) {
        every { refreshAccessToken(any(), any()) } just runs
        every { refreshRefreshToken(any(), any()) } just runs
        every { refreshTokenIsValid() } returns true
        every { refreshToken } returns TestData.REFRESH_TOKEN
        every { clear() } just runs
    }
    private val profileRepository = ProfileRepositoryImpl(
        userService = networkModule.userService,
        sessionService = networkModule.sessionService,
        appStore = appStore
    )

    private val testScope = TestScope()

    /* get profile */
    @Test
    fun `profile with 200 status → return success with profile model`() =
        testScope.runTest {
            UserServiceMock {
                profile.success()
            }
            val expectedModel = ProfileModel(
                firstName = TestData.NAME,
                lastName = TestData.LAST_NAME,
                image = "${URL.BASE_URL}${URL.USER_SERVICE_BASE_URL}${TestData.USER_PHOTO_LINK}",
                email = TestData.EMAIL
            )

            val result = profileRepository.getProfile()
            assertTrue(result is Result.Success && result.data == expectedModel)
        }

    @Test
    fun `profile return internal error → return error`() =
        testScope.runTest {
            UserServiceMock {
                profile.internalError()
            }
            val result = profileRepository.getProfile()
            assertTrue(result is Result.Error)
        }

    /* change password */
    @Test
    fun `refresh token is valid, changePassword return success → return success`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.success()
            }
            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 0) { appStore.refreshAccessToken(any(), any()) }
            coVerify(exactly = 0) { appStore.refreshRefreshToken(any(), any()) }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, return refreshToken is not empty, changePassword return success → refresh access token, refresh refresh token, return success`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.success()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 1) {
                appStore.refreshRefreshToken(
                    TestData.REFRESH_TOKEN,
                    TestData.EXPIRE_TIME_REFRESH_TOKEN
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, current email, access token, refresh token is null, changePassword return success → refresh access token, refresh refresh token, return success`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.success()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
                every { email } returns null
                every { accessToken } returns null
                every { refreshToken } returns null
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 1) {
                appStore.refreshRefreshToken(
                    TestData.REFRESH_TOKEN,
                    TestData.EXPIRE_TIME_REFRESH_TOKEN
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, return refreshToken is null, changePassword return success → refresh access token, return success`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.successRefreshTokenNull()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
                every { refreshToken } returns null
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 0) { appStore.refreshRefreshToken(any(), any()) }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, refreshToken return null, changePassword return success → return success`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.internalError()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 0) { appStore.refreshAccessToken(any(), any()) }
            coVerify(exactly = 0) { appStore.refreshRefreshToken(any(), any()) }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `changePassword return internal error → return error`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.internalError()
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 0) { appStore.refreshAccessToken(any(), any()) }
            coVerify(exactly = 0) { appStore.refreshRefreshToken(any(), any()) }
            assertTrue(result is Result.Error)
        }

    @Test
    fun `changePassword return 404 code → return wrong password error`() =
        testScope.runTest {
            UserServiceMock {
                changePassword.notFound()
            }

            val result = profileRepository.changePassword(
                oldPassword = TestData.PASSWORD,
                newPassword = TestData.PASSWORD
            )
            coVerify(exactly = 0) { appStore.refreshAccessToken(any(), any()) }
            coVerify(exactly = 0) { appStore.refreshRefreshToken(any(), any()) }
            assertTrue(result is Result.Error && result.error == ChangePasswordError.WrongOldPassword)
        }

    /* edit profile */
    @Test
    fun `edit profile return success, byteArray is not empty → return success`() =
        testScope.runTest {
            UserServiceMock {
                editProfile.success()
            }
            val result = profileRepository.editProfile(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = byteArrayOf()
            )
            assertTrue(result is Result.Success)
        }

    @Test
    fun `edit profile return success, byteArray is empty →  return success`() =
        testScope.runTest {
            UserServiceMock {
                editProfile.success()
            }
            val result = profileRepository.editProfile(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = null
            )
            assertTrue(result is Result.Success)
        }

    @Test
    fun `registration return internal error, byteArray is not empty → return error`() =
        testScope.runTest {
            UserServiceMock {
                editProfile.internalError()
            }
            val result = profileRepository.editProfile(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = byteArrayOf()
            )
            assertTrue(result is Result.Error)
        }

    @Test
    fun `registration return internal error, byteArray is empty → return error`() =
        testScope.runTest {
            UserServiceMock {
                editProfile.internalError()
            }
            val result = profileRepository.editProfile(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = null
            )
            assertTrue(result is Result.Error)
        }

    /* logout */
    @Test
    fun `logout return success → clear local data`() = testScope.runTest {
        UserServiceMock {
            logout.success()
        }
        profileRepository.logout()
        coVerify(exactly = 1) { appStore.clear() }
        WireMock.verify(
            WireMock.exactly(1),
            WireMock.getRequestedFor(UserServiceMock.logout.pattern)
        )
    }

    @Test
    fun `logout with empty refresh token return success → clear local data`() = testScope.runTest {
        UserServiceMock {
            logout.success()
        }
        appStore.apply {
            every { refreshToken } returns null
        }
        profileRepository.logout()
        coVerify(exactly = 1) { appStore.clear() }
        WireMock.verify(
            WireMock.exactly(1),
            WireMock.getRequestedFor(UserServiceMock.logout.pattern)
        )
    }
}
