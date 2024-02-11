package com.technostore.feature_profile.edit_profile.presentation

import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_profile.business.ProfileRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class EditProfileEffectHandlerTest {

    private val profileRepositoryMock = mockk<ProfileRepository> {
        coEvery { editProfile(any(), any(), any()) } returns Result.Success()
    }
    private val editProfileEffectHandler = EditProfileEffectHandler(
        profileRepository = profileRepositoryMock
    )
    private val defaultState = EditProfileState()
    private val store = mockk<Store<EditProfileState, EditProfileEvent>>(relaxed = true)
    private val testScope = TestScope()

    /* OnChangeProfileClicked */
    @Test
    fun `event OnChangeProfileClicked → name is not empty, last name is not empty, byteArray is not null, start loading, editProfile return success → end loading, open previous page`() =
        testScope.runTest {
            val byteArray = byteArrayOf(Byte.MIN_VALUE)
            val event = EditProfileUiEvent.OnChangeProfileClicked(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = byteArray
            )
            editProfileEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.StartLoading) }
            coVerify(exactly = 1) {
                profileRepositoryMock.editProfile(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    byteArray = byteArray
                )
            }
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(EditProfileNews.OpenPreviousPage) }
        }

    @Test
    fun `event OnChangeProfileClicked → name is not empty, last name is not empty, byteArray is null, start loading, editProfile return success → end loading, open previous page`() =
        testScope.runTest {
            val event = EditProfileUiEvent.OnChangeProfileClicked(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = null
            )
            editProfileEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.StartLoading) }
            coVerify(exactly = 1) {
                profileRepositoryMock.editProfile(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    byteArray = null
                )
            }
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(EditProfileNews.OpenPreviousPage) }
        }

    @Test
    fun `event OnChangeProfileClicked → name is not empty, last name is not empty, byteArray is not null, start loading, editProfile return error → end loading, show error toast`() =
        testScope.runTest {
            profileRepositoryMock.apply {
                coEvery {
                    profileRepositoryMock.editProfile(
                        any(),
                        any(),
                        any()
                    )
                } returns Result.Error()
            }
            val byteArray = byteArrayOf(Byte.MIN_VALUE)
            val event = EditProfileUiEvent.OnChangeProfileClicked(
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                byteArray = byteArray
            )
            editProfileEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.StartLoading) }
            coVerify(exactly = 1) {
                profileRepositoryMock.editProfile(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    byteArray = byteArray
                )
            }
            coVerify(exactly = 1) { store.dispatch(EditProfileEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(EditProfileNews.ShowErrorToast) }
        }

    @Test
    fun `event OnChangeProfileClicked → name is empty → show error, not call editProfile`() =
        testScope.runTest {
            val byteArray = byteArrayOf(Byte.MIN_VALUE)
            val event = EditProfileUiEvent.OnChangeProfileClicked(
                name = "",
                lastName = TestData.LAST_NAME,
                byteArray = byteArray
            )
            editProfileEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.acceptNews(EditProfileNews.NameIsEmpty) }
            coVerify(exactly = 0) {
                profileRepositoryMock.editProfile(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    byteArray = byteArray
                )
            }
        }

    @Test
    fun `event OnChangeProfileClicked → name is not empty, last name is empty → show error, not call editProfile`() =
        testScope.runTest {
            val byteArray = byteArrayOf(Byte.MIN_VALUE)
            val event = EditProfileUiEvent.OnChangeProfileClicked(
                name = TestData.NAME,
                lastName = "",
                byteArray = byteArray
            )
            editProfileEffectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.acceptNews(EditProfileNews.LastNameIsEmpty) }
            coVerify(exactly = 0) {
                profileRepositoryMock.editProfile(
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    byteArray = byteArray
                )
            }
        }

    /* OnBackButtonClicked */
    @Test
    fun `event OnBackButtonClicked → open prev page`() = testScope.runTest {
        val event = EditProfileUiEvent.OnBackButtonClicked
        editProfileEffectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(EditProfileNews.OpenPreviousPage) }
    }
}