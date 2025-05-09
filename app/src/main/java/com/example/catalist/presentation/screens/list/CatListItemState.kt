package com.example.catalist.presentation.screens.list

data class CatListItemState(
    val race: String = "",
    val alternativeRaces: List<String> = emptyList(),
    val description: String = "",
    val attributes: List<String> = emptyList()
)