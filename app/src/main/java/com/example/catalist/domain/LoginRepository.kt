package com.example.catalist.domain

import com.example.catalist.data.login.UserData
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun checkLoginStatus(): Boolean

    suspend fun setLoginStatus(isLoggedIn: Boolean)

    fun getUserData(): Flow<UserData>

    suspend fun setUserData(userData: UserData)
}