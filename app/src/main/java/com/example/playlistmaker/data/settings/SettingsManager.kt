package com.example.playlistmaker.data.settings

import android.content.SharedPreferences

const val DAYNIGHT_SWITCHER_KEY = "key_for_daynight_switcher"

class SettingsManager(val sharedPrefs: SharedPreferences) {

    fun loadTheme(): Boolean {
        return sharedPrefs.getBoolean(DAYNIGHT_SWITCHER_KEY, false)
    }

    fun saveTheme(mode: Boolean){
        sharedPrefs.edit()
            .putBoolean(DAYNIGHT_SWITCHER_KEY, mode)
            .apply()
    }
}