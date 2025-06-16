package com.example.catalist.presentation.screens.quiz

sealed interface QuizAction {
    data class OnOptionClicked(val option: String) : QuizAction
    data object OnTimeRunOut : QuizAction
    data object OnQuizQuitClick : QuizAction
    data object OnDialogDismiss : QuizAction
}