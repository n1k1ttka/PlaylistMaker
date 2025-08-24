package com.example.playlistmaker.ui.main.view_model

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Domain.settings.SettingsInteractor

class MainViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    fun loadTheme(): Boolean {
        return interactor.loadTheme()
    }

    fun saveTheme(isDarkTheme: Boolean) {
        interactor.saveTheme(isDarkTheme)
    }
}