package com.technostore.feature_login.password_recovery_email.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryEmailViewModel @Inject constructor(
    initialState: PasswordRecoveryEmailState,
    reducer: PasswordRecoveryEmailReducer,
    effectHandler: PasswordRecoveryEmailEffectHandler
) : BaseViewModel() {
    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<PasswordRecoveryEmailState> = store.state

    fun nextClicked(email: String) {
        viewModelScope.launch {
            store.dispatch(PasswordRecoveryEmailUIEvent.OnNextClicked(email))
        }
    }
}