package com.example.playlistmaker.UI.settings.view_model

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val interactor = Creator.provideThemeInteractor(application)

    private val _themeState = MutableLiveData<Boolean>()
    val themeState: LiveData<Boolean> = _themeState

    init {
        _themeState.value = interactor.loadTheme()
    }

    fun getShareIntent(): Intent {
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getApplication<Application>().getString(R.string.course_url))
            type = "text/plain"
        }
    }

    fun getSupportIntent(): Intent {
        return Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getApplication<Application>().getString(R.string.my_mail)}")
            putExtra(Intent.EXTRA_SUBJECT, getApplication<Application>().getString(R.string.for_developer))
            putExtra(Intent.EXTRA_TEXT, getApplication<Application>().getString(R.string.thanks_developers))
        }
    }

    fun getContractIntent(): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(getApplication<Application>().getString(R.string.ya_offer)))
    }

    fun toggleTheme(isChecked: Boolean) {
        _themeState.value = isChecked
        interactor.saveTheme(isChecked)
    }
}