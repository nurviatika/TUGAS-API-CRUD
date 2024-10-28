package com.example.dicodingevents.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dicodingevents.R
import com.example.dicodingevents.data.SettingPreferences
import com.example.dicodingevents.ui.ViewModel.SettingsViewModel
import com.example.dicodingevents.ui.ViewModel.SettingsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingPreferences(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchDarkMode = view.findViewById<SwitchCompat>(R.id.switch_dark_mode)

        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.isDarkMode.collectLatest { isDarkModeActive ->
                switchDarkMode.isChecked = isDarkModeActive
            }
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewLifecycleOwner.lifecycleScope.launch {
                settingsViewModel.setDarkMode(isChecked)
                applyTheme(isChecked)
            }
        }
    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}