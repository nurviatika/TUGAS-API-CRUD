package com.example.dicodingevents.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevents.data.SettingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingPreferences) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> get() = _isDarkMode

    init {
        viewModelScope.launch {
            preferences.isDarkModeFlow.collect { darkMode ->
                _isDarkMode.value = darkMode
            }
        }
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        _isDarkMode.value = isDarkMode
        preferences.setDarkModePreference(isDarkMode)
    }
}
