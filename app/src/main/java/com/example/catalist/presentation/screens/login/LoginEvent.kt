package com.example.catalist.presentation.screens.login

sealed interface LoginEvent {
    data object OnSuccessfulLogin : LoginEvent
    data class Error(val message: String) : LoginEvent
}