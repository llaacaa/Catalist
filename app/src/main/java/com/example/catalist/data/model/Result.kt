package com.example.catalist.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val category: Int,
    val createdAt: Long,
    val nickname: String,
    val result: Double
)