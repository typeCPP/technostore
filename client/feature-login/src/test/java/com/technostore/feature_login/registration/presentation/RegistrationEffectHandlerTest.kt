package com.technostore.feature_login.registration.presentation

import com.technostore.arch.result.Result
import com.technostore.feature_login.business.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RegistrationEffectHandlerTest : RegistrationBaseTest() {
    private val notValidEmail = "testtesTemail"
    private val notValidPassword = "test123!"

    private val loginRepositoryMock = mockk<LoginRepository> {
        coEvery { checkEmailExists(any()) } returns Result.Success(false)
    }
    private val effectHandler = RegistrationEffectHandler(
        loginRepository = loginRepositoryMock
    )

    /* OnRegistrationClicked */

    @Test
    fun `event OnRegistrationClicked → email valid, passwords are valid, passwords are equals, start loading, check email return not exists → stop loading, open registration user info page`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = password
            )
            val expectedNews = RegistrationNews.OpenRegistrationDataPage(
                email = trimEmail,
                password = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepositoryMock.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.acceptNews(expectedNews) }
        }

    @Test
    fun `event OnRegistrationClicked → email valid, passwords are valid, passwords are equals, start loading, check email return true → stop loading, show email exists error`() =
        runTest {
            loginRepositoryMock.apply {
                coEvery { checkEmailExists(any()) } returns Result.Success(true)
            }
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepositoryMock.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailExists) }
        }

    @Test
    fun `event OnRegistrationClicked → email valid, passwords are valid, passwords are equals, start loading, check email return error → stop loading, show error`() =
        runTest {
            loginRepositoryMock.apply {
                coEvery { checkEmailExists(any()) } returns Result.Error()
            }
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.StartLoading) }
            coVerify(exactly = 1) { loginRepositoryMock.checkEmailExists(trimEmail) }
            coVerify(exactly = 1) { store.acceptNews(RegistrationNews.ShowErrorToast) }
        }

    @Test
    fun `event OnRegistrationClicked → email valid, passwords are valid, passwords are not equals → show password are not equals error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = password + "1"
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.PasswordsAreBroken) }
            coVerify(exactly = 0) { loginRepositoryMock.checkEmailExists(trimEmail) }
        }

    @Test
    fun `event OnRegistrationClicked → second password is more than 255 symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = password.repeat(255)
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordErrorMaxLength) }
        }

    @Test
    fun `event OnRegistrationClicked → second password is less than 6 symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = "a"
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordErrorMinLength) }
        }

    @Test
    fun `event OnRegistrationClicked → second password is empty → show error`() = runTest {
        val event = RegistrationUiEvent.OnRegistrationClicked(
            email = email,
            firstPassword = password,
            secondPassword = ""
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsEmpty) }
    }

    @Test
    fun `event OnRegistrationClicked → second password have not valid symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password,
                secondPassword = notValidPassword
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordErrorSymbols) }
        }

    @Test
    fun `event OnRegistrationClicked → first password is more than 255 symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = password.repeat(255),
                secondPassword = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordErrorMaxLength) }
        }

    @Test
    fun `event OnRegistrationClicked → first password is less than 6 symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = "a",
                secondPassword = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordErrorMinLength) }
        }

    @Test
    fun `event OnRegistrationClicked → first password is empty → show error`() = runTest {
        val event = RegistrationUiEvent.OnRegistrationClicked(
            email = email,
            firstPassword = "",
            secondPassword = password
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsEmpty) }
    }

    @Test
    fun `event OnRegistrationClicked → first password have not valid symbols → show error`() =
        runTest {
            val event = RegistrationUiEvent.OnRegistrationClicked(
                email = email,
                firstPassword = notValidPassword,
                secondPassword = password
            )
            effectHandler.process(event, defaultState, store)
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsValid) }
            coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordErrorSymbols) }
        }

    @Test
    fun `event OnRegistrationClicked → email has invalid symbols → show error`() = runTest {
        val event = RegistrationUiEvent.OnRegistrationClicked(
            email = notValidEmail,
            firstPassword = password,
            secondPassword = password
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsInvalid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
    }

    @Test
    fun `event OnRegistrationClicked → email have more than 255 symbols → show error`() = runTest {
        val event = RegistrationUiEvent.OnRegistrationClicked(
            email = notValidEmail.repeat(255),
            firstPassword = password,
            secondPassword = password
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailMaxLength) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
    }

    @Test
    fun `event OnRegistrationClicked → email is empty → show error`() = runTest {
        val event = RegistrationUiEvent.OnRegistrationClicked(
            email = "  ",
            firstPassword = password,
            secondPassword = password
        )
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.EmailIsEmpty) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.FirstPasswordIsValid) }
        coVerify(exactly = 1) { store.dispatch(RegistrationEvent.SecondPasswordIsValid) }
    }

    /* OnLoginClicked */
    @Test
    fun `event on login clicked → open sign in page`() = runTest {
        val event = RegistrationUiEvent.OnLoginClicked
        effectHandler.process(event, defaultState, store)
        coVerify(exactly = 1) { store.acceptNews(RegistrationNews.OpenSignInPage) }
    }
}