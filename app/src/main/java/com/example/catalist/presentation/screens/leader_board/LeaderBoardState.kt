package com.example.catalist.presentation.screens.leader_board

data class LeaderBoardState(
    val items: List<LeaderBoardItemState> = emptyList(),
    val isLoading: Boolean = true,
)

data class LeaderBoardItemState(
    val nickname: String,
    val score: Int,
    val quizzesPlayed: Int
)

