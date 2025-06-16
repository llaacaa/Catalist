package com.example.catalist.domain

import com.example.catalist.data.LeaderBoardItemState
import com.example.catalist.data.model.ResultResponse

interface LeaderBoardRepository {
    suspend fun getAllResults(): Result<List<LeaderBoardItemState>>

    suspend fun sendResult(
        nickname: String,
        score: Double,
    ): Result<ResultResponse>
}