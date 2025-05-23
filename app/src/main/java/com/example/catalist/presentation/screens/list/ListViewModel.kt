package com.example.catalist.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.data.model.CatListResponseItemDto
import com.example.catalist.domain.CatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ListViewModel(
    private val repo: CatRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ListScreenState())
    val state = _state.asStateFlow()

    private val _channel = Channel<Unit>()
    val errorChannel = _channel.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        initData()
    }

    private fun initData() {
        searchJob?.cancel()
        searchJob  = viewModelScope.launch {
            setLoading(true)
            val response = repo.getAllBreeds()

            response.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        items = it.map { it.toUiModel() }
                    )
                },
                onFailure = {
                    _channel.send(Unit)
                }
            )
            setLoading(false)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _state.value = _state.value.copy(
            searchQuery = query
        )

        if (query.isBlank()) {
            initData()
            return
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            setLoading(true)
            delay(200L)
            val response = repo.searchBreeds(query)

            response.fold(
                onSuccess = {
                    _state.value = _state.value.copy(
                        items = it.map { it.toUiModel() }
                    )
                },
                onFailure = {
                    _channel.send(Unit)
                }
            )

            setLoading(false)
        }

    }

    private fun setLoading(loading: Boolean) {
        _state.value = _state.value.copy(
            isLoading = loading
        )
    }

}


fun CatListResponseItemDto.toUiModel(): CatListItemState {

    return CatListItemState(
        id = this.id ?: UUID.randomUUID().toString(),
        race = name ?: "No Name",
        alternativeRaces = alt_names ?: "",
        description = description?.take(250) ?: "No Description",
        temperaments = temperament?.split(",")?.take(5)?.map { it.trim() } ?: emptyList()
    )
}