package com.technostore.feature_profile.edit_profile.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    initialState: EditProfileState,
    reducer: EditProfileReducer,
    effectHandler: EditProfileEffectHandler
) : BaseViewModel() {

    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<EditProfileState> = store.state

    fun imageChanged(uri: Uri?) {
        viewModelScope.launch {
            store.dispatch(EditProfileUiEvent.OnImageChanged(uri))
        }
    }

    fun changedClicked(
        byteArray: ByteArray?,
        name: String,
        lastName: String
    ) {
        viewModelScope.launch {
            store.dispatch(
                EditProfileUiEvent.OnChangeProfileClicked(
                    byteArray = byteArray,
                    name = name,
                    lastName = lastName
                )
            )
        }
    }
}