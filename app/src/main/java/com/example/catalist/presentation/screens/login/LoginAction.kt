package com.example.catalist.presentation.screens.login

sealed interface LoginAction {
    data class OnEmailFieldChanged(val value: String) : LoginAction
    data class OnNicknameFieldChanged(val value: String) : LoginAction
    data class OnNameFieldChanged(val value: String) : LoginAction
    data class OnLastNameFieldChanged(val value: String) : LoginAction
    data object OnLogin : LoginAction
}
