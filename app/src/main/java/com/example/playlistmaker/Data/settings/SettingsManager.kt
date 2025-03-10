package com.example.playlistmaker.Data.settings

import android.content.Context

const val DAYNIGHT_SWITCHER_KEY = "key_for_daynight_switcher"

class SettingsManager(context: Context) {
    private val sharedPrefs = context.getSharedPreferences(DAYNIGHT_SWITCHER_KEY, Context.MODE_PRIVATE)

    fun loadTheme(): Boolean {
        return sharedPrefs.getBoolean(DAYNIGHT_SWITCHER_KEY, false)
    }

    fun saveTheme(mode: Boolean){
        sharedPrefs.edit()
            .putBoolean(DAYNIGHT_SWITCHER_KEY, mode)
            .apply()
    }
}