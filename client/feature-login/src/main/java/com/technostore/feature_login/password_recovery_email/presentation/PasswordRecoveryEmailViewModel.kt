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
    private val store: Store<PasswordRecoveryEmailState, PasswordRecoveryEmailEvent>
) : BaseViewModel() {

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