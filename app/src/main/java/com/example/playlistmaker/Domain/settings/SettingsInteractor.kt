package com.example.playlistmaker.Domain.settings

interface SettingsInteractor {

    fun saveTheme(mode: Boolean)

    fun loadTheme(): Boolean
}