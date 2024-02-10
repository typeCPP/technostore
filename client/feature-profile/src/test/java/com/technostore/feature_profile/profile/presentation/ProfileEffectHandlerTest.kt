package com.technostore.feature_profile.profile.presentation

import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_profile.business.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProfileEffectHandlerTest : ProfileBaseTest() {
    private val profileRepository = mockk<ProfileRepository> {
        coEvery { getProfile() } returns Result.Success(profileModel)
        coEvery { logout() } just runs
    }
    private val effectHandler = ProfileEffectHandler(profileRepository)

    /* Init */
    @Test
    fun `event Init → start loading, get profile return success → end loading, set data`() =
        runTest {
            val event = ProfileUiEvent.Init

            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(ProfileEvent.StartLoading) }
            coVerify(exactly = 1) { profileRepository.getProfile() }
            coVerify(exactly = 1) { store.dispatch(ProfileEvent.EndLoading) }
            coVerify(exactly = 1) {
                store.dispatch(
                    ProfileEvent.ProfileLoaded(
                        name = TestData.NAME,
                        lastName = TestData.LAST_NAME,
                        email = TestData.EMAIL,
                        image = TestData.USER_PHOTO_LINK
                    )
                )
            }
        }

    @Test
    fun `event Init → start loading, get profile return success without body → show error toast`() =
        runTest {
            profileRepository.apply {
                coEvery { getProfile() } returns Result.Success()
            }
            val event = ProfileUiEvent.Init

            effectHandler.process(event, defaultState, store)

            coVerify(exactly = 1) { store.dispatch(ProfileEvent.StartLoading) }
            coVerify(exactly = 1) { profileRepository.getProfile() }
            coVerify(exactly = 1) { store.acceptNews(ProfileNews.ShowErrorToast) }
        }

    @Test
    fun `event Init → start loading, get profile return error → show error toast`() = runTest {
        profileRepository.apply {
            coEvery { getProfile() } returns Result.Error()
        }
        val event = ProfileUiEvent.Init

        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.dispatch(ProfileEvent.StartLoading) }
        coVerify(exactly = 1) { profileRepository.getProfile() }
        coVerify(exactly = 1) { store.acceptNews(ProfileNews.ShowErrorToast) }
    }

    /* OnLogoutClicked */
    @Test
    fun `event OnLogoutClicked → call logout, logout user`() = runTest {
        val event = ProfileUiEvent.OnLogoutClicked

        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { profileRepository.logout() }
        coVerify(exactly = 1) { store.acceptNews(ProfileNews.Logout) }
    }

    /* OnChangeProfileClicked */
    @Test
    fun `event OnChangeProfileClicked → open change profile page`() = runTest {
        val event = ProfileUiEvent.OnChangeProfileClicked
        val currentState = defaultState.copy(
            name = TestData.NAME,
            lastName = TestData.LAST_NAME,
            image = TestData.USER_PHOTO_LINK
        )
        effectHandler.process(event, currentState, store)

        coVerify(exactly = 1) {
            store.acceptNews(
                ProfileNews.OpenChangeProfilePage(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    photoUrl = TestData.USER_PHOTO_LINK
                )
            )
        }
    }

    /* OnChangePasswordClicked */
    @Test
    fun `event OnChangePasswordClicked → open change password page`() = runTest {
        val event = ProfileUiEvent.OnChangePasswordClicked
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(ProfileNews.OpenChangePasswordPage) }
    }

    /* OnOrderHistoryClicked */
    @Test
    fun `event OnOrderHistoryClicked → open order history page`() = runTest {
        val event = ProfileUiEvent.OnOrderHistoryClicked
        effectHandler.process(event, defaultState, store)

        coVerify(exactly = 1) { store.acceptNews(ProfileNews.OpenOrderHistoryPage) }
    }
}