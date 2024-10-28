package com.example.dicodingevents

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("Settings")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    suspend fun saveDarkModeSetting(isDarkMode:Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDarkMode
        }
    }

}