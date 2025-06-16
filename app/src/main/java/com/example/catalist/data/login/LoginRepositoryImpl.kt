package com.example.catalist.data.login

import com.example.catalist.domain.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LoginRepositoryImpl(
    private val dataStore: LoginDataStore
) : LoginRepository {

    override suspend fun checkLoginStatus(): Boolean =
        dataStore.isLoggedIn.first()

    override suspend fun setLoginStatus(isLoggedIn: Boolean) =
        dataStore.updateLoginStatus(isLoggedIn)

    override fun getUserData(): Flow<UserData> =
        dataStore.userData

    override suspend fun setUserData(userData: UserData) =
        dataStore.updateUserData(userData)
}