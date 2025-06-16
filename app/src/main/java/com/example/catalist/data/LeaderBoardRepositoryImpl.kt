package com.example.catalist.data


import com.example.catalist.data.model.ResultResponse
import com.example.catalist.domain.LeaderBoardRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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

    override suspend fun sendResult(
        nickname: String,
        score: Double,
    ): Result<ResultResponse> {
        return try {
            val body = LeaderBoardRequest(
                nickname = nickname,
                result = score,
                category = 1
            )
            val json = Json {
            }
            json.encodeToString(body).also {
                println("CAOO LUCKO: " + it)
            }
            println(body)
            val response = client.post("https://rma.finlab.rs/leaderboard") {
                setBody(
                    body
                )
            }.body<ResultResponse>()

            Result.success(response)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}

@Serializable
data class LeaderBoardRequest(
    val nickname: String,
    val result: Double,
    val category: Int
)

@Serializable
data class LeaderBoardItemState(
    val category: Int,
    val nickname: String,
    val result: Double,
    val createdAt: Long
)

@Serializable
data class SendResultResponse(
    val result: LeaderBoardItemState,
    val ranking: Int
)
