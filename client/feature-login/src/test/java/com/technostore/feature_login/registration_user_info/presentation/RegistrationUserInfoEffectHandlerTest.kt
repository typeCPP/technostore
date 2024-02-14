package com.technostore.feature_login.registration_user_info.presentation

import android.net.Uri
import com.technostore.arch.mvi.Store
import com.technostore.arch.result.Result
import com.technostore.common_test.TestData
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RegistrationUserInfoEffectHandlerTest {
    private val loginRepository = mockk<LoginRepository> {
        coEvery { signUp(any(), any(), any(), any(), any()) } returns Result.Success()
    }
    private val effectHandler = RegistrationUserInfoEffectHandler(loginRepository)
    private val defaultState = RegistrationUserInfoState()
    private val store =
        mockk<Store<RegistrationUserInfoState, RegistrationUserInfoEvent>>(relaxed = true)

    /* OnImageChanged */
    @Test
    fun `event OnImageChanged with null uri → change image`() = runTest {
        val event = RegistrationUserInfoUiEvent.OnImageChanged(null)
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(RegistrationUserInfoNews.ChangeImage(null)) }
    }

    @Test
    fun `event OnImageChanged with not null uri → change image`() = runTest {
        val uri = Uri.EMPTY
        val event = RegistrationUserInfoUiEvent.OnImageChanged(uri)
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(RegistrationUserInfoNews.ChangeImage(uri)) }
    }

    /* OnRegistrationClicked */
    @Test
    fun `event OnRegistrationClicked → name and lastName are valid → start loading, signUp return success, end loading → open code page`() =
        runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.LastNameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 1) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(RegistrationUserInfoNews.OpenCodePage) }
        }

    @Test
    fun `event OnRegistrationClicked → name and lastName are valid → start loading, signUp return error, end loading → show error`() =
        runTest {
            loginRepository.apply {
                coEvery { signUp(any(), any(), any(), any(), any()) } returns Result.Error()
            }
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = TestData.NAME,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.LastNameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 1) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = TestData.NAME,
                    lastName = TestData.LAST_NAME,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(RegistrationUserInfoNews.ShowErrorToast) }
        }

    @Test
    fun `event OnRegistrationClicked → name is valid, lastName length more than 255 → show error`() =
        runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val lastName = "a".repeat(256)
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = TestData.NAME,
                lastName = lastName,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.LastNameErrorLength) }
            coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 0) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = TestData.NAME,
                    lastName = lastName,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
        }

    @Test
    fun `event OnRegistrationClicked → name is valid, lastName is empty → show error`() =
        runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val lastName = ""
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = TestData.NAME,
                lastName = lastName,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.LastNameIsEmpty) }
            coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 0) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = TestData.NAME,
                    lastName = lastName,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
        }

    @Test
    fun `event OnRegistrationClicked → name is valid, lastName is only spaces → show error`() =
        runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val lastName = "  "
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = TestData.NAME,
                lastName = lastName,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.LastNameIsEmpty) }
            coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 0) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = TestData.NAME,
                    lastName = lastName,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
        }

    @Test
    fun `event OnRegistrationClicked → name length more than 255 → show error`() = runTest {
        val byteArray = byteArrayOf(Byte.MAX_VALUE)
        val name = "a".repeat(256)
        val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
            byteArray = byteArray,
            name = name,
            lastName = TestData.LAST_NAME,
            email = TestData.EMAIL,
            password = TestData.PASSWORD
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameErrorLength) }
        coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
        coVerify(exactly = 0) {
            loginRepository.signUp(
                byteArray = byteArray,
                name = name,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
        }
    }

    @Test
    fun `event OnRegistrationClicked → name is empty → show error`() =
        runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val name = ""
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = name,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsEmpty) }
            coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 0) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = name,
                    lastName = TestData.LAST_NAME,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
        }

    @Test
    fun `event OnRegistrationClicked → name is only spaces → show error`() = runTest {
            val byteArray = byteArrayOf(Byte.MAX_VALUE)
            val name = "  "
            val event = RegistrationUserInfoUiEvent.OnRegistrationClicked(
                byteArray = byteArray,
                name = name,
                lastName = TestData.LAST_NAME,
                email = TestData.EMAIL,
                password = TestData.PASSWORD
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationUserInfoEvent.NameIsEmpty) }
            coVerify(exactly = 0) { store.dispatch(RegistrationUserInfoEvent.StartLoading) }
            coVerify(exactly = 0) {
                loginRepository.signUp(
                    byteArray = byteArray,
                    name = name,
                    lastName = TestData.LAST_NAME,
                    email = TestData.EMAIL,
                    password = TestData.PASSWORD
                )
            }
        }
}