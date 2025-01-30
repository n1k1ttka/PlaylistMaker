package com.example.playlistmaker.Domain

interface ThemeInteractor {

    fun saveTheme(mode: Boolean)

    fun loadTheme(): Boolean
}