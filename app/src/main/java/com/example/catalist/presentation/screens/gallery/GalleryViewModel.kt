package com.example.catalist.presentation.screens.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.CatRepository
import com.example.catalist.presentation.screens.details.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryRepository: CatRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    init {
        val id = savedStateHandle.get<String>("id") ?: ""
        initData(id)
    }

    private fun initData(id: String) {
        viewModelScope.launch {
            galleryRepository.getCatImagesFor(id).fold(
                onSuccess = { images ->
                    _state.update {
                        it.copy(
                            images = images.map {
                                Image(url = it.url)
                            }
                        )
                    }
                },
                onFailure = {
                }
            )
        }
    }

    fun onAction(action: GalleryAction) {
        when (action) {
            is GalleryAction.OnImageClick -> {
                val image = _state.value.images.firstOrNull { it.url == action.imageUrl } ?: return

                _state.update {
                    it.copy(
                        isInPagerViewMode = true,
                        clickedImageIndex = _state.value.images.indexOf(image)
                    )
                }

            }
            GalleryAction.OnExitPagerViewMode -> {
                _state.update {
                    it.copy(
                        isInPagerViewMode = false,
                        clickedImageIndex = INVALID_INDEX
                    )
                }
            }
        }
    }


}