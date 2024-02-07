package com.technostore.feature_login.business

import LoginServiceMock
import com.technostore.app_store.store.AppStore
import com.technostore.arch.result.ErrorType
import com.technostore.arch.result.Result
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.common_test.mock.SessionServiceMock
import com.technostore.common_test.network.NetworkModuleTest
import com.technostore.feature_login.business.sign_in.error.Message
import com.technostore.feature_login.business.sign_in.error.SignInError
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

private const val CODE = "1234"

@ExperimentalCoroutinesApi
class LoginRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()

    private val appStore = mockk<AppStore>(relaxed = true) {
        every { refresh(any(), any(), any(), any(), any(), any()) } just runs
        every { id } returns TestData.ID.toString()
        every { refreshAccessToken(any(), any()) } just runs
        every { refreshRefreshToken(any(), any()) } just runs
        every { refreshTokenIsValid() } returns true
        every { refreshToken } returns TestData.REFRESH_TOKEN
    }
    private val loginRepository = LoginRepositoryImpl(
        loginService = networkModule.loginService,
        appStore = appStore,
        sessionService = networkModule.sessionService,
        userService = networkModule.userService,
    )

    private val testScope = TestScope()

    /* Sign in */
    @Test
    fun `sign in with 200 status → refresh data, set isActive, return success`() =
        testScope.runTest {
            LoginServiceMock {
                login.success()
            }
            val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
            coVerify(exactly = 1) { appStore.isActive = true }
            coVerify(exactly = 1) {
                appStore.refresh(
                    refreshToken = TestData.REFRESH_TOKEN,
                    accessToken = TestData.ACCESS_TOKEN,
                    expireTimeRefreshToken = TestData.EXPIRE_TIME_REFRESH_TOKEN,
                    expireTimeAccessToken = TestData.EXPIRE_TIME_ACCESS_TOKEN,
                    id = TestData.ID.toString(),
                    email = TestData.EMAIL
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `sign in with 404 status (wrong password) → return error password`() = testScope.runTest {
        LoginServiceMock {
            login.errorPassword(Message.ERROR_PASSWORD)
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
        assertTrue(Result.Error<SignInError>(SignInError.ErrorPassword) == result)
    }

    @Test
    fun `sign in with 404 status (wrong email) → return error email`() = testScope.runTest {
        LoginServiceMock {
            login.errorEmail(Message.ERROR_EMAIL)
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }

        assertTrue(Result.Error<SignInError>(SignInError.ErrorEmail) == result)
    }

    @Test
    fun `sign in with 500 status → return error`() = testScope.runTest {
        LoginServiceMock {
            login.internalError()
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }

        assertTrue(Result.Error<Unit>() == result)
    }

    @Test
    fun `check emailt exists return true → return true`() = testScope.runTest {
        LoginServiceMock {
            checkEmailExists.successExists()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)

        assertTrue(Result.Success(true) == result)
    }

    /* check email exists */

    @Test
    fun `check emailt exists return false → return false`() = testScope.runTest {
        LoginServiceMock {
            checkEmailExists.successNotExists()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)

        assertTrue(Result.Success(false) == result)
    }

    @Test
    fun `check emailt with 500 status → return error`() = testScope.runTest {
        LoginServiceMock {
            checkEmailExists.internalError()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)
        assertTrue(result is Result.Error<Boolean> && result.error is ErrorType)
    }

    /* registration */

    @Test
    fun `registration success, byteArray is not empty → refresh data, return success`() =
        testScope.runTest {
            LoginServiceMock {
                registration.success()
            }
            val result = loginRepository.signUp(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD,
                byteArray = byteArrayOf()
            )
            coVerify(exactly = 1) {
                appStore.refresh(
                    refreshToken = TestData.REFRESH_TOKEN,
                    accessToken = TestData.ACCESS_TOKEN,
                    expireTimeRefreshToken = TestData.EXPIRE_TIME_REFRESH_TOKEN,
                    expireTimeAccessToken = TestData.EXPIRE_TIME_ACCESS_TOKEN,
                    id = TestData.ID.toString(),
                    email = TestData.EMAIL
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `registration success, byteArray is empty → refresh data, return success`() =
        testScope.runTest {
            LoginServiceMock {
                registration.success()
            }
            val result = loginRepository.signUp(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD,
                byteArray = null
            )
            coVerify(exactly = 1) {
                appStore.refresh(
                    refreshToken = TestData.REFRESH_TOKEN,
                    accessToken = TestData.ACCESS_TOKEN,
                    expireTimeRefreshToken = TestData.EXPIRE_TIME_REFRESH_TOKEN,
                    expireTimeAccessToken = TestData.EXPIRE_TIME_ACCESS_TOKEN,
                    id = TestData.ID.toString(),
                    email = TestData.EMAIL
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `registration internal error, byteArray is not empty → return error`() =
        testScope.runTest {
            LoginServiceMock {
                registration.internalError()
            }
            val result = loginRepository.signUp(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD,
                byteArray = byteArrayOf()
            )
            coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
            assertTrue(result is Result.Error)
        }

    @Test
    fun `registration internal error, byteArray is empty → return error`() =
        testScope.runTest {
            LoginServiceMock {
                registration.internalError()
            }
            val result = loginRepository.signUp(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD,
                byteArray = null
            )
            coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
            assertTrue(result is Result.Error)
        }

    /* checkRecoveryCodeForAccountConfirmations */
    @Test
    fun `confirmAccount returns success → return success true`() =
        testScope.runTest {
            LoginServiceMock {
                confirmAccount.success()
            }
            val result = loginRepository.checkRecoveryCodeForAccountConfirmations(
                email = TestData.EMAIL,
                code = CODE
            )
            assertTrue(result == Result.Success(true))
        }

    @Test
    fun `confirmAccount returns 404 → return success false`() =
        testScope.runTest {
            LoginServiceMock {
                confirmAccount.notFound()
            }
            val result = loginRepository.checkRecoveryCodeForAccountConfirmations(
                email = TestData.EMAIL,
                code = CODE
            )
            assertTrue(result == Result.Success(false))
        }

    @Test
    fun `confirmAccount returns internal error → return error`() =
        testScope.runTest {
            LoginServiceMock {
                confirmAccount.internalError()
            }
            val result = loginRepository.checkRecoveryCodeForAccountConfirmations(
                email = TestData.EMAIL,
                code = CODE
            )
            assertTrue(result is Result.Error)
        }

    /* changePassword */
    @Test
    fun `refresh token is valid, changePassword return success → return success`() =
        testScope.runTest {
            LoginServiceMock {
                changePassword.success()
            }
            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, return refreshToken is not empty, changePassword return success → refresh access token, refresh refresh token, return success`() =
        testScope.runTest {
            LoginServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.success()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
            }

            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 1) {
                appStore.refreshRefreshToken(
                    any(),
                    any()
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, current email, access token, refresh token is null, changePassword return success → refresh access token, refresh refresh token, return success`() =
        testScope.runTest {
            LoginServiceMock {
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

            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 1) {
                appStore.refreshRefreshToken(
                    any(),
                    any()
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, return refreshToken is null, changePassword return success → refresh access token, return success`() =
        testScope.runTest {
            LoginServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.successRefreshTokenNull()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
                every { refreshToken } returns null
            }

            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            coVerify(exactly = 1) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 0) {
                appStore.refreshRefreshToken(
                    any(),
                    any()
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `refresh token is not valid, refreshToken return null, changePassword return success → return success`() =
        testScope.runTest {
            LoginServiceMock {
                changePassword.success()
            }
            SessionServiceMock {
                refreshTokens.internalError()
            }
            appStore.apply {
                every { refreshTokenIsValid() } returns false
            }

            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            coVerify(exactly = 0) {
                appStore.refreshAccessToken(
                    any(),
                    any()
                )
            }
            coVerify(exactly = 0) {
                appStore.refreshRefreshToken(
                    any(),
                    any()
                )
            }
            assertTrue(result is Result.Success)
        }

    @Test
    fun `changePassword return error → return error`() =
        testScope.runTest {
            LoginServiceMock {
                changePassword.internalError()
            }

            val result = loginRepository.changePassword(newPassword = TestData.PASSWORD)
            coVerify(exactly = 0) {
                appStore.refreshAccessToken(
                    TestData.ACCESS_TOKEN,
                    TestData.EXPIRE_TIME_ACCESS_TOKEN
                )
            }
            coVerify(exactly = 0) {
                appStore.refreshRefreshToken(
                    any(),
                    any()
                )
            }
            assertTrue(result is Result.Error)
        }


    /* sendCodeForRecoveryPassword */
    @Test
    fun `sendCodeForRecoveryPassword returns success → return success`() =
        testScope.runTest {
            LoginServiceMock {
                sendCodeForRecoveryPassword.success()
            }
            val result = loginRepository.sendCodeForRecoveryPassword(email = TestData.EMAIL)
            assertTrue(result is Result.Success)
        }

    @Test
    fun `sendCodeForRecoveryPassword returns internal error → return error`() =
        testScope.runTest {
            LoginServiceMock {
                sendCodeForRecoveryPassword.internalError()
            }
            val result = loginRepository.sendCodeForRecoveryPassword(email = TestData.EMAIL)
            assertTrue(result is Result.Error)
        }

    /* checkRecoveryCode */
    @Test
    fun `check recovery code returns success → return success true`() =
        testScope.runTest {
            LoginServiceMock {
                passwordRecovery.success()
            }
            val result = loginRepository.checkRecoveryCode(code = CODE, email = TestData.EMAIL)
            coVerify(exactly = 1) {
                appStore.refresh(
                    refreshToken = TestData.REFRESH_TOKEN,
                    accessToken = TestData.ACCESS_TOKEN,
                    expireTimeRefreshToken = TestData.EXPIRE_TIME_REFRESH_TOKEN,
                    expireTimeAccessToken = TestData.EXPIRE_TIME_ACCESS_TOKEN,
                    id = TestData.ID.toString(),
                    email = TestData.EMAIL
                )
            }
            assertTrue(result == Result.Success(true))
        }

    @Test
    fun `check recovery code returns 409 code → return success false`() =
        testScope.runTest {
            LoginServiceMock {
                passwordRecovery.conflict()
            }
            val result = loginRepository.checkRecoveryCode(code = CODE, email = TestData.EMAIL)
            coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
            assertTrue(result == Result.Success(false))
        }

    @Test
    fun `check recovery code returns internal error → return error`() =
        testScope.runTest {
            LoginServiceMock {
                passwordRecovery.internalError()
            }
            val result = loginRepository.checkRecoveryCode(code = CODE, email = TestData.EMAIL)
            coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
            assertTrue(result is Result.Error)
        }


    /* sendCodeForAccountConfirmations */
    @Test
    fun `sendCodeForAccountConfirmations, id is null → return error`() =
        testScope.runTest {
            appStore.apply {
                every { id } returns null
            }
            val result = loginRepository.sendCodeForAccountConfirmations()
            assertTrue(result is Result.Error)
        }

    @Test
    fun `sendCodeForAccountConfirmations, id is empty → return error`() =
        testScope.runTest {
            appStore.apply {
                every { id } returns ""
            }
            val result = loginRepository.sendCodeForAccountConfirmations()
            assertTrue(result is Result.Error)
        }

    @Test
    fun `sendCodeForAccountConfirmations returns success → return success`() =
        testScope.runTest {
            LoginServiceMock {
                sendCodeForConfirmationAccount.success()
            }
            val result = loginRepository.sendCodeForAccountConfirmations()
            assertTrue(result is Result.Success)
        }

    @Test
    fun `sendCodeForAccountConfirmations returns internal error → return error`() =
        testScope.runTest {
            LoginServiceMock {
                sendCodeForConfirmationAccount.internalError()
            }
            val result = loginRepository.sendCodeForAccountConfirmations()
            assertTrue(result is Result.Error)
        }
}