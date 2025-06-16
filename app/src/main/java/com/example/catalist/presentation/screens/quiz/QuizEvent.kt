package com.example.catalist.presentation.screens.quiz

sealed interface QuizEvent {
    data object OnQuizCompleted : QuizEvent
    data class OnQuizError(val message: String) : QuizEvent
    data object OnNavigateToLeaderboard : QuizEvent
}