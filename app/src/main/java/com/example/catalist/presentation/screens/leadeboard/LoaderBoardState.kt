package com.example.catalist.presentation.screens.leadeboard



data class LeaderBoardState (
    val isLoading: Boolean = false,
    val leaderboardData: List<LeaderBoardItemState> = emptyList()
)


data class LeaderBoardItemState(
    val nickname: String,
    val result: Double,
    val quizzesTaken: Int = 0,
)
