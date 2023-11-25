package com.technostore.feature_login.password_recovery_code.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryCodeViewModel @Inject constructor(
    initialState: PasswordRecoveryCodeState,
    reducer: PasswordRecoveryCodeReducer,
    effectHandler: PasswordRecoveryCodeEffectHandler
) : BaseViewModel() {
    private val store = Store(
        initialState = initialState,
        reducer = reducer,
        effectHandlers = listOf(effectHandler)
    )

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<PasswordRecoveryCodeState> = store.state

    fun onNextClicked(email: String, code: String) {
        viewModelScope.launch {
            store.dispatch(PasswordRecoveryCodeUiEvent.OnNextClicked(email, code))
        }
    }

    fun onRepeatClicked(email: String) {
        viewModelScope.launch {
            store.dispatch(PasswordRecoveryCodeUiEvent.OnRepeatClicked(email))
        }
    }
}