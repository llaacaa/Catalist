package com.example.catalist.domain

import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.data.model.ImageResponseDtoItem

interface CatRepository {

    suspend fun getAllBreeds(): Result<List<CatListResponseItemDto>>

    suspend fun searchBreeds(query: String): Result<List<CatListResponseItemDto>>

    suspend fun getBreedBy(id: String): Result<CatListResponseItemDto>

    suspend fun getCatImageById(id: String): Result<List<ImageResponseDtoItem>>
}