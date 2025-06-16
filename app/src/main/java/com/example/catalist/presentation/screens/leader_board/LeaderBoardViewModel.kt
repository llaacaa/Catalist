package com.example.catalist.presentation.screens.leader_board


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.LeaderBoardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderBoardViewModel(
    private val repo: LeaderBoardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderBoardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repo.getAllResults()
                .onSuccess { list ->
                    _state.value = LeaderBoardState(
                        items = list.sortedByDescending { it.score },
                        isLoading = false
                    )
                }
                .onFailure {
                    _state.value = LeaderBoardState(isLoading = false)
                }
        }
    }
}