package com.example.catalist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey
    val id: String,
    val width: Int,
    val height: Int,
    val url: String,
    val catId: String
)
