package com.example.catalist.domain

import com.example.catalist.presentation.screens.list.CatListItemState

interface CatRepository {

    suspend fun getAllCats(): Result<List<CatListItemState>>

    suspend fun getAllCatsBy(race: String): Result<List<CatListItemState>>

}