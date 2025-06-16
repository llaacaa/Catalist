package com.example.catalist.presentation.screens.profile

data class ProfileState (
    val firstName : String = "",
    val lastName : String = "",
    val email : String = "",
    val allResults: List<ResultItemState> = emptyList(),
    val bestResult: String = "",
    val bestLeaderBoardPosition: Int = 0,
)

data class ResultItemState(
    val score: String,
    val date: String,
)