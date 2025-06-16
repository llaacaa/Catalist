package com.example.catalist.data

import com.example.catalist.data.local.CatDao
import com.example.catalist.data.local.CatDatabase
import com.example.catalist.data.local.Image
import com.example.catalist.data.local.toDto
import com.example.catalist.data.local.toEntity
import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.data.model.ImageResponseDtoItem
import com.example.catalist.domain.CatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class CatRepositoryImpl(
    private val client: HttpClient,
    private val db: CatDatabase,
    private val catDao: CatDao = db.catDao(),
) : CatRepository {
    override suspend fun getAllBreeds(): Result<List<CatListResponseItemDto>> =
        withContext(Dispatchers.IO) {
            try {

                val isDbEmpty = catDao.getAllCats().isEmpty()

                if (isDbEmpty) {
                    val body: List<CatListResponseItemDto> = client.get("v1/breeds").body()
                    val entities = body.map { it.toEntity() }
                    catDao.insertCats(entities)
                }

                Result.success(catDao.getAllCats().map { it.toDto() }.sortedBy { it.name })
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

    override suspend fun getBreedBy(id: String): Result<CatListResponseItemDto> =
        withContext(Dispatchers.IO) {
            try {
                val cat = catDao.getCatById(id) ?: client.get("/v1/breeds/$id").body()

                Result.success(cat.toDto())
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }

    override suspend fun getCatImageById(id: String): Result<List<ImageResponseDtoItem>> =
        withContext(Dispatchers.IO) {
            try {
                val images = catDao.getAllImagesBy(id).takeIf { it.isNotEmpty() }?.map {
                    it.toCatImageResponseDtoItem()
                }
                    ?: client.get("/v1/images/search?breed_ids=$id").body()

                catDao.insertImages(images.map { it.toEntity(id) })
                Result.success(catDao.getAllImagesBy(id).map { it.toCatImageResponseDtoItem() })
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }

    override suspend fun getCatImagesFor(id: String)
            : Result<List<ImageResponseDtoItem>> = withContext(Dispatchers.IO)
    {
        try {
            val images = catDao.getAllImagesBy(id).takeIf { it.size > 1 }?.map {
                it.toCatImageResponseDtoItem()
            }
                ?: client.get("https://api.thecatapi.com/v1/images/search?breed_ids=$id&format=json&limit=10")
                    .body()

            catDao.insertImages(images.map { it.toEntity(id) })

            Result.success(catDao.getAllImagesBy(id).map { it.toCatImageResponseDtoItem() })
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }
}

fun Image.toCatImageResponseDtoItem(): ImageResponseDtoItem {
    return ImageResponseDtoItem(
        height = this.height,
        id = this.id,
        url = this.url,
        width = this.width,
    )
}

fun ImageResponseDtoItem.toEntity(catId: String): Image {
    return Image(
        id = this.id,
        url = this.url,
        height = this.height,
        width = this.width,
        catId = catId
    )
}