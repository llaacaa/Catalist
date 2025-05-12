package com.example.catalist.presentation.screens.list

data class CatListItemState(
    val id: String = "",
    val race: String = "",
    val alternativeRaces: String = "",
    val description: String = "",
    val temperaments: List<String> = emptyList()
)