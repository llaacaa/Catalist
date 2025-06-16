package com.example.catalist.domain

import com.example.catalist.presentation.screens.leader_board.LeaderBoardItemState

interface LeaderBoardRepository {
    suspend fun getAllResults(): Result<List<LeaderBoardItemState>>
}