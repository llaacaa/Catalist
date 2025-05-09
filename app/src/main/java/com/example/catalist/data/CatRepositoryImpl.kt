package com.example.catalist.data

import com.example.catalist.domain.CatRepository
import com.example.catalist.presentation.screens.list.CatListItemState

class CatRepositoryImpl: CatRepository {
    override suspend fun getAllCats(): Result<List<CatListItemState>> {
        return Result.success(emptyList())
    }

    override suspend fun getAllCatsBy(race: String): Result<List<CatListItemState>> {
        return Result.success(emptyList())
    }
}