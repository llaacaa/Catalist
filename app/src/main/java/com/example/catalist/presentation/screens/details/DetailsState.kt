package com.example.catalist.presentation.screens.details

data class DetailsState(
    val image: String = "",
    val name: String = "",
    val description: String = "",
    val originCountries: String = "",
    val temperament: List<String> = listOf(),
    val lifespan: String = "",
    val weight: String = "",
    val behavior: HashMap<String, Int> = hashMapOf(),
    val isRare: Int = 0,
    val wikiUrl: String = "",
    val isLoading: Boolean = false
)
