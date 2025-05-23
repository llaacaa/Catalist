package com.example.catalist.data

import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.data.model.ImageResponseDtoItem
import com.example.catalist.domain.CatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

class CatRepositoryImpl(
    private val client: HttpClient
) : CatRepository {
    override suspend fun getAllBreeds(): Result<List<CatListResponseItemDto>> {
        return try {
            val body: List<CatListResponseItemDto> = client.get("v1/breeds").body()

            Result.success(body)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun searchBreeds(query: String): Result<List<CatListResponseItemDto>> {
        return try {
            val body: List<CatListResponseItemDto> = client.get("/v1/breeds/search") {
                url {
                    parameters.append("q", query)
                }
            }.body()

            Result.success(body)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun getBreedBy(id: String): Result<CatListResponseItemDto> {
        return try {
            val body: CatListResponseItemDto = client.get("/v1/breeds/$id").body()

            Result.success(body)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    override suspend fun getCatImageById(id: String): Result<List<ImageResponseDtoItem>> {
        return try {
            val body: List<ImageResponseDtoItem> =
                client.get("/v1/images/search?breed_ids=$id").body()

            Result.success(body)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}
