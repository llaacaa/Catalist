package com.example.catalist.presentation.screens.gallery

sealed interface GalleryAction {
    data object OnExitPagerViewMode : GalleryAction
    data class OnImageClick(val imageUrl: String) : GalleryAction

}