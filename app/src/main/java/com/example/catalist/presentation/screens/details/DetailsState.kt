package com.example.catalist.presentation.screens.details

data class DetailsState(
    val id: String = "",
    val name: String = "",
    val image : Image = Image(),
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

data class Image(
    val url: String = "",
    val aspectRatio: Float = 1f
)