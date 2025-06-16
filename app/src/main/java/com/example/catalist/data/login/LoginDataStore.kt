package com.example.catalist.data.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginDataStore(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

    private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val KEY_FIRST_NAME   = stringPreferencesKey("first_name")
    private val KEY_LAST_NAME    = stringPreferencesKey("last_name")
    private val KEY_NICKNAME     = stringPreferencesKey("nickname")
    private val KEY_EMAIL        = stringPreferencesKey("email")

    val isLoggedIn: Flow<Boolean>
        get() = context.dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }

    val userData: Flow<UserData>
        get() = context.dataStore.data.map { prefs ->
            UserData(
                firstName = prefs[KEY_FIRST_NAME] ?: "",
                lastName  = prefs[KEY_LAST_NAME]  ?: "",
                nickname  = prefs[KEY_NICKNAME]   ?: "",
                email     = prefs[KEY_EMAIL]      ?: ""
            )
        }

    suspend fun updateLoginStatus(value: Boolean) {
        context.dataStore.edit { it[KEY_IS_LOGGED_IN] = value }
    }

    suspend fun updateUserData(data: UserData) {
        context.dataStore.edit {
            it[KEY_FIRST_NAME] = data.firstName
            it[KEY_LAST_NAME]  = data.lastName
            it[KEY_NICKNAME]   = data.nickname
            it[KEY_EMAIL]      = data.email
        }
    }
}