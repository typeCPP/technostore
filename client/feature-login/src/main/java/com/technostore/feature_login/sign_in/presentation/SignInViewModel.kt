package com.technostore.feature_login.sign_in.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    initialState: SignInState,
    reducer: SignInReducer,
    effectHandler: SignInEffectHandler
) : BaseViewModel() {
    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<SignInState> = store.state

    fun signInClicked(email: String?, password: String?) {
        viewModelScope.launch {
            store.dispatch(
                SignInUiEvent.OnSignInClicked(
                    email = email,
                    password = password
                )
            )
        }
    }

    fun forgotPasswordClicked() {
        viewModelScope.launch {
            store.dispatch(SignInUiEvent.OnForgotPasswordClicked)
        }
    }

    fun registrationClicked() {
        viewModelScope.launch {
            store.dispatch(SignInUiEvent.OnRegistrationClicked)
        }
    }
}