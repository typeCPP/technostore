package com.technostore.feature_login.password_recovery.presentation

import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class PasswordRecoveryEffectHandlerTest : PasswordRecoveryBaseTest() {
    private val loginRepository = mockk<LoginRepository> {
        coEvery { changePassword(any()) } returns Result.Success()
    }
    private val notValidPassword = "test123!"
    private val effectHandler = PasswordRecoveryEffectHandler(loginRepository)

    /* OnNextClicked */
    @Test
    fun `event OnNextClicked → passwords are valid, passwords are equals, start loading, change password return success, stop loading → open login page`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = firstPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.changePassword(firstPassword) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryNews.OpenLoginPage) }
        }

    @Test
    fun `event OnNextClicked → passwords are valid, passwords are equals, start loadingchange password return error, stop loading → stop loading, show error toast`() =
        runTest {
            loginRepository.apply {
                coEvery { changePassword(any()) } returns Result.Error()
            }
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = firstPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepository.changePassword(firstPassword) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.EndLoading) }
            coVerify(exactly = 1) { store.acceptNews(PasswordRecoveryNews.ShowErrorToast) }
        }

    @Test
    fun `event OnNextClicked → passwords are valid, passwords are not equals → show password are not equals error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = secondPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.PasswordsAreBroken) }
        }

    @Test
    fun `event OnNextClicked → second password is more than 255 symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = secondPassword.repeat(255)
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorMaxLength) }
        }

    @Test
    fun `event OnNextClicked → second password is less than 6 symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = "a"
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorMinLength) }
        }

    @Test
    fun `event OnNextClicked → second password is empty → show error`() = runTest {
        val event = PasswordRecoveryUIEvent.OnNextClicked(
            firstPassword = firstPassword,
            secondPassword = ""
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordIsEmpty) }
    }

    @Test
    fun `event OnNextClicked → second password have not valid symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword,
                secondPassword = notValidPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.SecondPasswordErrorSymbols) }
        }

    @Test
    fun `event OnNextClicked → first password is more than 255 symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = firstPassword.repeat(255),
                secondPassword = firstPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorMaxLength) }
        }

    @Test
    fun `event OnNextClicked → first password is less than 6 symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = "a",
                secondPassword = firstPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorMinLength) }
        }

    @Test
    fun `event OnNextClicked → first password is empty → show error`() = runTest {
        val event = PasswordRecoveryUIEvent.OnNextClicked(
            firstPassword = "",
            secondPassword = firstPassword
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordIsEmpty) }
    }

    @Test
    fun `event OnNextClicked → first password have not valid symbols → show error`() =
        runTest {
            val event = PasswordRecoveryUIEvent.OnNextClicked(
                firstPassword = notValidPassword,
                secondPassword = firstPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(PasswordRecoveryEvent.FirstPasswordErrorSymbols) }
        }
}