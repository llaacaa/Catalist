package com.example.catalist.presentation.screens.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalist.domain.LoginRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _channel = Channel<LoginEvent>()
    val events = _channel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLogin -> {
                viewModelScope.launch {
                    if (state.value.canLogIn) {
                        loginRepository.setLoginStatus(true)
                        _channel.send(LoginEvent.OnSuccessfulLogin)
                        return@launch
                    }

                    // TODO handle nickname validation
                    val validateNick = false
                    if (Patterns.EMAIL_ADDRESS.matcher(state.value.email).matches()) {
                        _channel.send(LoginEvent.Error("Email Adress is not valid"))
                    } else if (validateNick) {
                        _channel.send(LoginEvent.Error("Nicnkname is not valid"))
                    } else {
                        _channel.send(LoginEvent.Error("Please fill all the fields"))

                    }
                }
            }

            is LoginAction.OnNameFieldChanged -> {
                _state.update {
                    it.copy(
                        name = action.value
                    )
                }
            }

            is LoginAction.OnEmailFieldChanged -> {
                _state.update {
                    it.copy(
                        email = action.value
                    )
                }
            }

            is LoginAction.OnLastNameFieldChanged -> {
                _state.update {
                    it.copy(
                        lastName = action.value
                    )
                }
            }

            is LoginAction.OnNicknameFieldChanged -> {
                _state.update {
                    it.copy(
                        nickname = action.value
                    )
                }
            }
        }
    }
}