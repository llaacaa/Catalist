package com.example.catalist.data


import com.example.catalist.domain.LeaderBoardRepository
import com.example.catalist.presentation.screens.leader_board.LeaderBoardItemState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

class LeaderBoardRepositoryImpl(
    private val client: HttpClient
) : LeaderBoardRepository {
    override suspend fun getAllResults(): Result<List<LeaderBoardItemState>> {
        return try {
            val body: List<LeaderBoardItemState> = client.get("https://rma.finlab.rs/leaderboard?category=1").body()

            Result.success(body)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}