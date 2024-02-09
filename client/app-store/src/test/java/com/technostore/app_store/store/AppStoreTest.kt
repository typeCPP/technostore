package com.technostore.app_store.store

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val REFRESH_TOKEN_RESULT = "refresh-token"
private const val ACCESS_TOKEN_RESULT = "access-token"
private const val EMAIL_RESULT = "email"
private const val ID_RESULT = "12"
private const val IS_ACTIVE_RESULT = true
private const val IS_ONBOARDING_SHOWN_RESULT = true
private const val EXPIRE_TIME_REFRESH_TOKEN_RESULT = 1234L
private const val EXPIRE_TIME_ACCESS_TOKEN_RESULT = 1234L
private const val CURRENT_DATE_ACCESS_TOKEN_RESULT = 1234L
private const val CURRENT_DATE_REFRESH_TOKEN_RESULT = 1234L

class AppStoreTest {
    private val sharedPrev = mockk<SharedPreferences>() {
        every { getString(AppStoreImpl.REFRESH_TOKEN, null) } returns REFRESH_TOKEN_RESULT
        every { getString(AppStoreImpl.ACCESS_TOKEN, null) } returns ACCESS_TOKEN_RESULT
        every { getString(AppStoreImpl.EMAIL, null) } returns EMAIL_RESULT
        every { getString(AppStoreImpl.ID, null) } returns ID_RESULT
        every { getBoolean(AppStoreImpl.IS_ACTIVE, false) } returns IS_ACTIVE_RESULT
        every {
            getBoolean(
                AppStoreImpl.IS_ONBOARDING_SHOWN,
                false
            )
        } returns IS_ONBOARDING_SHOWN_RESULT
        every {
            getLong(
                AppStoreImpl.EXPIRE_TIME_ACCESS_TOKEN,
                0
            )
        } returns EXPIRE_TIME_ACCESS_TOKEN_RESULT
        every {
            getLong(
                AppStoreImpl.EXPIRE_TIME_REFRESH_TOKEN,
                0
            )
        } returns EXPIRE_TIME_REFRESH_TOKEN_RESULT
        every {
            getLong(
                AppStoreImpl.CURRENT_DATE_ACCESS_TOKEN,
                0
            )
        } returns CURRENT_DATE_ACCESS_TOKEN_RESULT
        every {
            getLong(
                AppStoreImpl.CURRENT_DATE_REFRESH_TOKEN,
                0
            )
        } returns CURRENT_DATE_REFRESH_TOKEN_RESULT
    }
    private val appStore = AppStoreImpl(sharedPrev)

    /* refresh token */
    @Test
    fun `get refresh token, refresh token is not empty  → return refreshToken`() {
        val refreshToken = appStore.refreshToken
        assertEquals(REFRESH_TOKEN_RESULT, refreshToken)
    }

    @Test
    fun `get refresh token, refresh token is empty → return null`() {
        sharedPrev.apply {
            every { getString(AppStoreImpl.REFRESH_TOKEN, null) } returns null
        }
        val refreshToken = appStore.refreshToken
        assertEquals(null, refreshToken)
    }

    /* access token */
    @Test
    fun `get access token, access token is not empty → return access token`() {
        val accessToken = appStore.accessToken
        assertEquals(ACCESS_TOKEN_RESULT, accessToken)
    }

    @Test
    fun `get access token, access token is empty → return null`() {
        sharedPrev.apply {
            every { getString(AppStoreImpl.ACCESS_TOKEN, null) } returns null
        }
        val accessToken = appStore.accessToken
        assertEquals(null, accessToken)
    }
    /* email */

    @Test
    fun `get email, email is not empty → return email`() {
        val value = appStore.email
        assertEquals(EMAIL_RESULT, value)
    }

    @Test
    fun `get email, email is empty → return null`() {
        sharedPrev.apply {
            every { getString(AppStoreImpl.EMAIL, null) } returns null
        }
        val value = appStore.email
        assertEquals(null, value)
    }

    /* id */
    @Test
    fun `get id, id is not empty → return id`() {
        val value = appStore.id
        assertEquals(ID_RESULT, value)
    }

    @Test
    fun `get id, id is empty → return null`() {
        sharedPrev.apply {
            every { getString(AppStoreImpl.ID, null) } returns null
        }
        val value = appStore.id
        assertEquals(null, value)
    }

    /* isActive */
    @Test
    fun `get is active, isActive is not empty → return isActive`() {
        val value = appStore.isActive
        assertEquals(IS_ACTIVE_RESULT, value)
    }

    @Test
    fun `get is active, isActive is empty → return false`() {
        sharedPrev.apply {
            every { getBoolean(AppStoreImpl.IS_ACTIVE, false) } returns false
        }
        val value = appStore.isActive
        assertEquals(false, value)
    }

    @Test
    fun `get isOnboardingShown, isOnboardingShown is not empty → return isOnboardingShown`() {
        val value = appStore.isOnboardingShown
        assertEquals(IS_ONBOARDING_SHOWN_RESULT, value)
    }

    /* isOnboardingShown */
    @Test
    fun `get isOnboardingShown, isOnboardingShown is empty → return false`() {
        sharedPrev.apply {
            every { getBoolean(AppStoreImpl.IS_ONBOARDING_SHOWN, false) } returns false
        }
        val value = appStore.isOnboardingShown
        assertEquals(false, value)
    }
}