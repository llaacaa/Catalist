package com.example.catalist.presentation.screens.details

data class DetailsState(
    val imageUrl: String = "",
    val imageRatio: Float = 1f,
    val name: String = "",
    val description: String = "",
    val origin: String = "",
    val temperament: List<String> = listOf(),
    val lifespan: String = "",
    val weight: String = "",
    val behavior: Map<String, Int> = emptyMap(),
    val isRare: Boolean = true,
    val wikiUrl: String = "",
    val isLoading: Boolean = false
)
