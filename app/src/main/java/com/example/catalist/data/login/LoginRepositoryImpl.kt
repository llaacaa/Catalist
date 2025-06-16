package com.example.catalist.data.login

import com.example.catalist.domain.LoginRepository
import kotlinx.coroutines.flow.first

class LoginRepositoryImpl(
    private val dataStore: LoginDataStore
) : LoginRepository {

    override suspend fun checkLoginStatus(): Boolean {
        return dataStore.data.first()
    }

    override suspend fun setLoginStatus(isLoggedIn: Boolean) {
        dataStore.update(isLoggedIn)
    }
}