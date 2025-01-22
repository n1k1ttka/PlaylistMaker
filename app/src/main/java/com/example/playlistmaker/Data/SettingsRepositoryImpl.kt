package com.example.playlistmaker.Data

import com.example.playlistmaker.Domain.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsManager: SettingsManager
): SettingsRepository{
    override fun saveTheme(mode: Boolean) {
        settingsManager.saveTheme(mode)
    }

    override fun loadTheme(): Boolean {
        return settingsManager.loadTheme()
    }

}