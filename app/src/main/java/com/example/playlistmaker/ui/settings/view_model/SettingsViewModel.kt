package com.example.playlistmaker.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Domain.settings.SettingsInteractor

class SettingsViewModel(
    private val interactor: SettingsInteractor,
    private val courseUrl: String,
    private val myMail: String,
    private val forDeveloper: String,
    private val thanksDevelopers: String,
    private val yaOffer: String
) : ViewModel() {

    private val _themeState = MutableLiveData<Boolean>()
    val themeState: LiveData<Boolean> = _themeState

    init {
        _themeState.value = interactor.loadTheme()
    }

    fun getShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, courseUrl)
            type = "text/plain"
        }
    }

    fun getSupportIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$myMail")
            putExtra(Intent.EXTRA_SUBJECT, forDeveloper)
            putExtra(Intent.EXTRA_TEXT, thanksDevelopers)
        }
    }

    fun getContractIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(yaOffer))
    }

    fun toggleTheme(isChecked: Boolean) {
        _themeState.value = isChecked
        interactor.saveTheme(isChecked)
    }
}