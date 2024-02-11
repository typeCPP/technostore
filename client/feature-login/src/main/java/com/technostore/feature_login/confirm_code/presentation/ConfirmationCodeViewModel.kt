package com.technostore.feature_login.confirm_code.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCodeViewModel @Inject constructor(
    private val store: Store<ConfirmationCodeState, ConfirmationCodeEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ConfirmationCodeState> = store.state
    fun checkRecoveryCode(email: String, code: String) {
        viewModelScope.launch {
            store.dispatch(
                ConfirmationCodeUIEvent.OnConfirmCode(
                    email = email,
                    code = code
                )
            )
        }
    }
}