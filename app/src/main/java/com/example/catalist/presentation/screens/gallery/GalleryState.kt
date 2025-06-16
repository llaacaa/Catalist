package com.example.catalist.presentation.screens.gallery

import com.example.catalist.presentation.screens.details.Image

const val INVALID_INDEX = -1

data class GalleryState(
    val images: List<Image> = emptyList(),
    val isInPagerViewMode: Boolean = false,
    val clickedImageIndex: Int = INVALID_INDEX,
)

