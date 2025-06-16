package com.example.catalist.domain

interface LoginRepository {

    suspend fun checkLoginStatus(): Boolean

    suspend fun setLoginStatus(isLoggedIn: Boolean)
}