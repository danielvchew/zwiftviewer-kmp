package com.danielchew.zwiftviewer.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsernameChanged(new: String) {
        _uiState.update { it.copy(username = new) }
    }

    fun onPasswordChanged(new: String) {
        _uiState.update { it.copy(password = new) }
    }

    fun onLoginClicked(): Boolean {
        // For now, just return true always
        return true
    }
}