package com.technostore.feature_login.registration.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    val store: Store<RegistrationState, RegistrationEvent>
) : BaseViewModel() {
    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<RegistrationState> = store.state

    fun registrationClicked(email: String, firstPassword: String, secondPassword: String) {
        viewModelScope.launch {
            store.dispatch(
                RegistrationUiEvent.OnRegistrationClicked(
                    email = email,
                    firstPassword = firstPassword,
                    secondPassword = secondPassword
                )
            )
        }
    }

    fun loginClicked() {
        viewModelScope.launch {
            store.dispatch(RegistrationUiEvent.OnLoginClicked)
        }
    }
}