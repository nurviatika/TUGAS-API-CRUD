package com.example.dicodingevents.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dicodingevents.R
import com.example.dicodingevents.data.SettingPreferences
import com.example.dicodingevents.ui.EventFragment
import com.example.dicodingevents.ui.FavoriteFragment
import com.example.dicodingevents.ui.SettingsFragment
import com.example.dicodingevents.ui.ViewModel.SettingsViewModel
import com.example.dicodingevents.ui.ViewModel.SettingsViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var settingPreferences: SettingPreferences

    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(SettingPreferences(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            settingsViewModel.isDarkMode.collect { isDarkModeActive ->
                applyTheme(isDarkModeActive)
            }
        }
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)


        if (savedInstanceState == null) {
            loadFragment(EventFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_upcoming -> {
                    val fragevent = EventFragment()
                    val bundle = Bundle()
                    bundle.putInt("key_active", 1)
                    fragevent.arguments = bundle
                    loadFragment(fragevent)
                    true
                }
                R.id.navigation_finished -> {
                    val fragment = EventFragment()
                    val bundle = Bundle()
                    bundle.putInt("key_active", 0)
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    true
                }
                R.id.navigation_favorite -> {
                    loadFragment(FavoriteFragment())
                    true
                }

                R.id.navigation_setting -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun applyTheme(isDarkModeActive: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeActive) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
