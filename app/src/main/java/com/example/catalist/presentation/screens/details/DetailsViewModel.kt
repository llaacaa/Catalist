package com.example.catalist.presentation.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repo: CatRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    fun initData(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val response = repo.getBreedBy(id)

            response.fold(
                onSuccess = {
                    _state.value.copy(

                    )
                },
                onFailure = {

                }
            )

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

}

