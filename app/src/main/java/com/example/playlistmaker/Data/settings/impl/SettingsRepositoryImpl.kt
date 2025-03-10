package com.example.playlistmaker.Data.settings.impl

import com.example.playlistmaker.Data.settings.SettingsManager
import com.example.playlistmaker.Domain.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val settingsManager: SettingsManager
): SettingsRepository {
    override fun saveTheme(mode: Boolean) {
        settingsManager.saveTheme(mode)
    }

    override fun loadTheme(): Boolean {
        return settingsManager.loadTheme()
    }

}