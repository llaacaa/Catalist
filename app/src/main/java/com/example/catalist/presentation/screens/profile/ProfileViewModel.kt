package com.example.catalist.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.LoginRepository
import com.example.catalist.domain.ResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileViewModel(
    private val loginRepository: LoginRepository,
    private val resultRepository: ResultRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {


        viewModelScope.launch {
            val dataFromDB = resultRepository.getQuizResults()

            val allResults = dataFromDB.map {
                it.score.toFloat()
            }

            val bestLeaderBoardPosition = dataFromDB.map {
                it.ranking ?: 0
            }.max()

            val user = loginRepository.getUserData().first()

            _state.update {
                it.copy(
                    allResults = dataFromDB.map { result ->
                        ResultItemState(
                            score = result.score.toString(),
                            date  = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                .format(Date(result.createdAt))
                        )
                    },
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    bestResult = (allResults.maxOrNull() ?: 0f).toString(),
                    bestLeaderBoardPosition = bestLeaderBoardPosition
                )
            }

        }


    }
}