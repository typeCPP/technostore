package com.technostore.feature_login.registration_user_info.presentation

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationUserInfoViewModel @Inject constructor(
    initialState: RegistrationUserInfoState,
    reducer: RegistrationUserInfoReducer,
    effectHandler: RegistrationUserInfoEffectHandler
) : BaseViewModel() {
    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<RegistrationUserInfoState> = store.state

    fun imageChanged(uri: Uri?) {
        viewModelScope.launch {
            store.dispatch(RegistrationUserInfoUiEvent.OnImageChanged(uri))
        }
    }

    fun registrationClicked(
        byteArray: ByteArray?,
        email: String,
        password: String,
        name: String,
        lastName: String
    ) {
        viewModelScope.launch {
            store.dispatch(
                RegistrationUserInfoUiEvent.OnRegistrationClicked(
                    byteArray = byteArray,
                    email = email,
                    password = password,
                    name = name,
                    lastName = lastName
                )
            )
        }
    }
}