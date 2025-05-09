package com.example.catalist.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.CatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel(
    val repo: CatRepository
): ViewModel() {

    val mockData = buildList {
        repeat(10) {
            add(
                CatListItemState(
                    race = "Persijka",
                    alternativeRaces = listOf("cao", "sta ima"),
                    description = "ovo je macka ona ide na 4 noge nmp iskreno bla bla bla ima oci i usi lovi miseve",
                    attributes = listOf("playful", "angru", "mad")
                )
            )
        }
    }

    private val _state = MutableStateFlow(ListScreenState())
    val state = _state.asStateFlow()

    init {
       viewModelScope.launch {
           _state.value = _state.value.copy(
               isLoading = true
           )

           delay(1500L)
           repo.getAllCats()

           _state.value = _state.value.copy(
               isLoading = false
           )

           _state.update {
               it.copy(
                   items = mockData
               )
           }
       }
    }
}