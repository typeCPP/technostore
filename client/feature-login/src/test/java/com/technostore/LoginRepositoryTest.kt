package com.technostore

import LoginServiceMock
import com.technostore.arch.result.Result
import com.technostore.app_store.store.AppStore
import com.google.common.truth.Truth.assertThat
import com.technostore.arch.result.ErrorType
import com.technostore.common_test.MockServer
import com.technostore.common_test.TestData
import com.technostore.feature_login.business.LoginRepositoryImpl
import com.technostore.feature_login.business.sign_in.error.Message.ERROR_EMAIL
import com.technostore.feature_login.business.sign_in.error.SignInError
import com.technostore.common_test.network.NetworkModuleTest
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Rule
import com.technostore.feature_login.business.sign_in.error.Message.ERROR_PASSWORD
import kotlinx.coroutines.test.TestScope
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginRepositoryTest {
    private val networkModule = NetworkModuleTest()

    @get:Rule
    val wireMockRule = MockServer.testRule()

    private val appStore = mockk<AppStore>(relaxed = true) {
        every { refresh(any(), any(), any(), any(), any(), any()) } just runs
    }
    private val loginRepository = LoginRepositoryImpl(
        loginService = networkModule.loginService,
        appStore = appStore,
        sessionService = networkModule.sessionService,
        userService = networkModule.userService,
    )
    private val testStore = TestScope()

    /* Sign in */
    @Test
    fun `sign in with 200 status → refresh data, set isActive, return success`() =
        testStore.runTest {
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
            assertThat(result is Result.Success)
        }

    @Test
    fun `sign in with 404 status (wrong password) → return error password`() = runTest {
        LoginServiceMock {
            login.errorPassword(ERROR_PASSWORD)
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }
        assertTrue(Result.Error<SignInError>(SignInError.ErrorPassword) == result)
    }

    @Test
    fun `sign in with 404 status (wrong email) → return error email`() = runTest {
        LoginServiceMock {
            login.errorEmail(ERROR_EMAIL)
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }

        assertTrue(Result.Error<SignInError>(SignInError.ErrorEmail) == result)
    }

    @Test
    fun `sign in with 500 status → return error`() = runTest {
        LoginServiceMock {
            login.internalError()
        }
        val result = loginRepository.signIn(TestData.EMAIL, TestData.PASSWORD)
        coVerify(exactly = 0) { appStore.isActive = true }
        coVerify(exactly = 0) { appStore.refresh(any(), any(), any(), any(), any(), any()) }

        assertTrue(Result.Error<Unit>() == result)
    }

    @Test
    fun `check emailt exists return true → return true`() = runTest {
        LoginServiceMock {
            checkEmailExists.successExists()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)

        assertTrue(Result.Success(true) == result)
    }

    /* check email exists */

    @Test
    fun `check emailt exists return false → return false`() = runTest {
        LoginServiceMock {
            checkEmailExists.successNotExists()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)

        assertTrue(Result.Success(false) == result)
    }

    @Test
    fun `check emailt with 500 status → return error`() = runTest {
        LoginServiceMock {
            checkEmailExists.internalError()
        }
        val result = loginRepository.checkEmailExists(TestData.EMAIL)
        assertTrue(result is Result.Error<Boolean> && result.error is ErrorType)
    }
}