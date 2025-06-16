package com.example.catalist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WeightDto(
    val imperial: String?,
    val metric: String?
)