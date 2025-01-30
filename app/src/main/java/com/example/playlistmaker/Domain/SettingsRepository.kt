package com.example.playlistmaker.Domain

interface SettingsRepository {
    fun saveTheme(mode: Boolean)
    fun loadTheme(): Boolean
}