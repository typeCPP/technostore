package com.technostore.feature_profile.edit_profile.presentation

import android.net.Uri
import com.technostore.arch.mvi.Store
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditProfileViewModelTest {
    private val store = mockk<Store<EditProfileState, EditProfileEvent>>(relaxed = true)
    private val viewModel = EditProfileViewModel(store)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `image changed with null uri → send OnImageChanged event`() = runTest {
        viewModel.imageChanged(null)
        advanceUntilIdle()
        coVerify { store.dispatch(EditProfileUiEvent.OnImageChanged(null)) }
    }
    @Test
    fun `image changed with not null uri → send OnImageChanged event`() = runTest {
        val uri=Uri.EMPTY
        viewModel.imageChanged(uri)
        advanceUntilIdle()
        coVerify { store.dispatch(EditProfileUiEvent.OnImageChanged(uri)) }
    }


    @Test
    fun `change clicked → send OnChangeProfileClicked event`() = runTest {
        val byteArray = byteArrayOf(Byte.MIN_VALUE)
        val name = "new name"
        val lastName = "new last name"
        viewModel.changedClicked(
            byteArray = byteArray,
            name = name,
            lastName = lastName
        )
        val expectedEvent = EditProfileUiEvent.OnChangeProfileClicked(
            byteArray = byteArray,
            name = name,
            lastName = lastName
        )
        advanceUntilIdle()
        coVerify { store.dispatch(expectedEvent) }
    }
}