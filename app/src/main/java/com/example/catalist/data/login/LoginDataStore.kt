package com.example.catalist.data.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginDataStore(
    private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

    private val KEY = booleanPreferencesKey("example_counter")

     val data: Flow<Boolean>
        get() = context.dataStore.data.map {
            it[KEY] ?: false
        }

    suspend fun update(value: Boolean) {
        context.dataStore.edit {
            it[KEY] = value
        }
    }
}