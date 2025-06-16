package com.example.catalist.presentation.screens.quiz

import com.example.catalist.presentation.screens.details.Image

data class QuizState(
    val currentQuestionNumber: Int = 0,
    val currentQuestion: QuizQuestion = QuizQuestion(),
    val isLoading: Boolean = true,
    val timer: String = ""
)

data class QuizQuestion(
    val type: QuestionType = QuestionType.GUESS_THE_RACE,
    val catImg: Image = Image(),
    val options: List<String> = emptyList(),
    val correctAnswer: String = "",
)

enum class QuestionType {
    GUESS_THE_RACE,
    ODD_ONE_OUT,
}
