package com.example.playlistmaker.Domain

class ThemeInteractorImpl(
    private val settingsRepository: SettingsRepository
): ThemeInteractor {
    override fun saveTheme(mode: Boolean) {
        settingsRepository.saveTheme(mode)
    }

    override fun loadTheme(): Boolean {
        return settingsRepository.loadTheme()
    }
}