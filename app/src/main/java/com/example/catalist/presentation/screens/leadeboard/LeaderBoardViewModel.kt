package com.example.catalist.presentation.screens.leadeboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.LeaderBoardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderBoardViewModel(
    private val leaderBoardRepository: LeaderBoardRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(LeaderBoardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {

            val allResults = leaderBoardRepository.getAllResults().getOrNull()?.map {
                LeaderBoardItemState(
                    nickname = it.nickname,
                    result = it.result
                )
            } ?: emptyList()

            val quizzesPlayedPerPerson = allResults.groupBy { it.nickname }
                .mapValues { (_, results) -> results.size }

            val result = allResults.map {
                it.copy(
                    quizzesTaken = quizzesPlayedPerPerson[it.nickname] ?: 0
                )
            }

            _state.update {
                it.copy(
                    isLoading = false,
                    leaderboardData = result

                )
            }
        }
    }
}