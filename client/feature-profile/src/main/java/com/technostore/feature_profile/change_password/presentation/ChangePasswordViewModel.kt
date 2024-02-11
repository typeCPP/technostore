package com.technostore.feature_profile.change_password.presentation

import androidx.lifecycle.viewModelScope
import com.technostore.arch.mvi.BaseViewModel
import com.technostore.arch.mvi.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val store: Store<ChangePasswordState, ChangePasswordEvent>
) : BaseViewModel() {

    init {
        store.setViewModel(this)
    }

    val viewState: StateFlow<ChangePasswordState> = store.state

    fun onChangedClicked(
        oldPassword: String,
        newPassword: String,
        newRepeatPassword: String
    ) {
        viewModelScope.launch {
            store.dispatch(
                ChangePasswordUiEvent.OnChangePasswordClicked(
                    oldPassword = oldPassword,
                    newPassword = newPassword,
                    newRepeatPassword = newRepeatPassword
                )
            )
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            store.dispatch(ChangePasswordUiEvent.OnBackButtonClicked)
        }
    }
}