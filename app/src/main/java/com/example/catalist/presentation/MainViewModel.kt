package com.example.catalist.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.LoginRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    data class MainState(
        val isLoading: Boolean = false,
        val isLoggedIn: Boolean = false,
    )

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true
            )
            state = state.copy(
                isLoggedIn = loginRepository.checkLoginStatus()
            )
            state = state.copy(
                isLoading = false
            )
        }
    }
}
