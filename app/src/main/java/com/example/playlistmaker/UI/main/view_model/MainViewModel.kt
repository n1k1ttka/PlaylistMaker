package com.example.playlistmaker.UI.main.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.Domain.settings.SettingsInteractor

class MainViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    fun loadTheme(): Boolean {
        return interactor.loadTheme()
    }

    fun saveTheme(isDarkTheme: Boolean) {
        interactor.saveTheme(isDarkTheme)
    }

    companion object {
        fun getViewModelFactory(interactor: SettingsInteractor): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(interactor)
            }
        }
    }
}