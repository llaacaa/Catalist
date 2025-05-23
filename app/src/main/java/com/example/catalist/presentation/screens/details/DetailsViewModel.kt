package com.example.catalist.presentation.screens.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.domain.CatRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repo: CatRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    private val _channel = Channel<Unit>()
    val errorChannel = _channel.receiveAsFlow()

    init {
        val id = savedStateHandle.get<String>("id") ?: ""
        initData(id)
    }

    private fun initData(id: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )
            val detailsResponse = repo.getBreedBy(id)
            val imageResponse = repo.getCatImageById(id)


            detailsResponse.fold(
                onSuccess = {
                    _state.value = it.toDetails()
                },
                onFailure = {
                    _channel.send(Unit)
                }
            )

            imageResponse.fold(
                onSuccess = {
                    val imageItem = it.firstOrNull() ?: return@launch
                    _state.value = _state.value.copy(
                        imageUrl = imageItem.url ?: "",
                        imageRatio = imageItem.width.toFloat() / imageItem.height.toFloat()
                    )
                },
                onFailure = {
                    _channel.send(Unit)
                }
            )

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

}

private fun CatListResponseItemDto.toDetails(): DetailsState {

    val map = mapOf(
        "Adaptability" to this.adaptability,
        "Affection Level" to this.affection_level,
        "Child Friendly" to this.child_friendly,
        "Dog Friendly" to this.dog_friendly,
        "Energy Level" to this.energy_level,
        "Grooming" to this.grooming,
        "Health Issues" to this.health_issues,
        "Intelligence" to this.intelligence,
        "Shedding Level" to this.shedding_level,
        "Social Needs" to this.social_needs,
        "Stranger Friendly" to this.stranger_friendly,
        "Vocalisation" to this.vocalisation
    )
        .mapNotNull { (key, value) ->
            value?.let {
                key to it
            }
        }
        .take(5)
        .associate { it.first to it.second }

    return DetailsState(
        name = this.name ?: "Name not found",
        description = this.description ?: "Description not found",
        origin = this.origin ?: "Origin not found",
        temperament = temperament?.split(",")?.take(5)?.map { it.trim() } ?: emptyList(),
        lifespan = this.life_span ?: "Lifespan not found",
        weight = this.weight?.metric?.let { "$it kg" } ?: "Weight not found",
        behavior = map,
        isRare = rare == 0,
        wikiUrl = this.wikipedia_url ?: ""
    ).also {
        println("DetailsState: $it")
    }
}