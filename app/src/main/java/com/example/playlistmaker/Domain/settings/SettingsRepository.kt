package com.example.playlistmaker.Domain.settings

interface SettingsRepository {
    fun saveTheme(mode: Boolean)
    fun loadTheme(): Boolean
}