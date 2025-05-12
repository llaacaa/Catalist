package com.example.catalist.data

import com.example.catalist.data.model.CatListResponseDto
import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.domain.CatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

class CatRepositoryImpl(
    private val client: HttpClient
): CatRepository {
    override suspend fun getAllBreeds(): Result<List<CatListResponseItemDto>> {
        return try {
            val body: List<CatListResponseItemDto> = client.get("v1/breeds").body()

            Result.success(body)
        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun searchBreeds(query: String): Result<List<CatListResponseItemDto>> {
        return try {
            val body: List<CatListResponseItemDto> = client.get("/v1/breeds/search") {
                parameters {
                    append("q", query)
                }
            }.body()

            Result.success(body)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBreedBy(id: String): Result<CatListResponseItemDto> {
        return try {
            val body: CatListResponseItemDto = client.get("/v1/breeds/$id").body()

            Result.success(body)
        } catch (e : Exception) {
            Result.failure(e)
        }
    }
}
