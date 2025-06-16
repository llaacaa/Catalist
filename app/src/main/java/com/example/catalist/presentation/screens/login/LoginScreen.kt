package com.example.catalist.presentation.screens.login

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    state: LoginState,
    onLoginAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.name,
            onValueChange = {
                onLoginAction(LoginAction.OnNameFieldChanged(it))
            },
            label = {
                Text("Name")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.lastName,
            onValueChange = {
                onLoginAction(LoginAction.OnLastNameFieldChanged(it))
            },
            label = {
                Text("Last Name")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),

            value = state.email,
            onValueChange = {
                onLoginAction(LoginAction.OnEmailFieldChanged(it))
            },
            label = {
                Text("Email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),

            value = state.nickname,
            onValueChange = {
                onLoginAction(LoginAction.OnNicknameFieldChanged(it))
            },

            label = {
                Text("Nickname")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.padding(4.dp),
            onClick = {
                onLoginAction(LoginAction.OnLogin)
            },
        ) {
            Text("Login")
        }

    }
}

data class LoginState(
    val email: String = "",
    val nickname: String = "",
    val name: String = "",
    val lastName: String = "",
) {
    val canLogIn = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            nickname.isNotBlank() &&
            name.isNotBlank() &&
            lastName.isNotBlank()
}
