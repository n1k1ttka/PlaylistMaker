package com.example.playlistmaker.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.main.view_model.MainViewModel
import com.example.playlistmaker.UI.player.view_model.MediaViewModel
import com.example.playlistmaker.UI.search.view_model.SearchViewModel
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(get())
    }

    factory <MediaPlayer> { // Можно ли сделать это single? Мне кажется что factory не оптимальное решение. Найти в интернете подсказки не смог
        MediaPlayer()
    }

    viewModel {
        MediaViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    single(named("courseUrl")) { androidContext().getString(R.string.course_url) }
    single(named("myMail")) { androidContext().getString(R.string.my_mail) }
    single(named("forDeveloper")) { androidContext().getString(R.string.for_developer) }
    single(named("thanksDevelopers")) { androidContext().getString(R.string.thanks_developers) }
    single(named("yaOffer")) { androidContext().getString(R.string.ya_offer) }

    viewModel {
        SettingsViewModel(
            get(),
            get(named("courseUrl")),
            get(named("myMail")),
            get(named("forDeveloper")),
            get(named("thanksDevelopers")),
            get(named("yaOffer"))
        )
    }
}