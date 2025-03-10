package com.example.playlistmaker.Domain.settings.impl

import com.example.playlistmaker.Domain.settings.SettingsRepository
import com.example.playlistmaker.Domain.settings.SettingsInteractor

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
): SettingsInteractor {
    override fun saveTheme(mode: Boolean) {
        settingsRepository.saveTheme(mode)
    }

    override fun loadTheme(): Boolean {
        return settingsRepository.loadTheme()
    }
}