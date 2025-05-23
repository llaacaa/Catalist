package com.example.catalist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponseDtoItem(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)