package com.example.catalist.presentation.screens.list

sealed interface ListAction {
    data object OnQuizClick : ListAction
    data object OnUserProfileClick : ListAction
}