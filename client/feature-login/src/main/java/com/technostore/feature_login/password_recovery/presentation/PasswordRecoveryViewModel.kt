package com.technostore.feature_login.password_recovery.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val store: Store<PasswordRecoveryState, PasswordRecoveryEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<PasswordRecoveryState> = store.state

    fun nextClicked(firstPassword: String, secondPassword: String) {
        viewModelScope.launch {
            store.dispatch(
                PasswordRecoveryUIEvent.OnNextClicked(
                    firstPassword = firstPassword,
                    secondPassword = secondPassword
                )
            )
        }
    }
}