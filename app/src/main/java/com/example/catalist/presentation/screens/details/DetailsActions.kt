package com.example.catalist.presentation.screens.details

sealed interface DetailsActions {
    data class OnGalleryClick(val id: String) : DetailsActions
}