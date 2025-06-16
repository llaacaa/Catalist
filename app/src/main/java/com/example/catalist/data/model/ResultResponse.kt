package com.example.catalist.data.model

import kotlinx.serialization.Serializable


@Serializable
data class ResultResponse(
    val ranking: Int,
    val result: Result
)