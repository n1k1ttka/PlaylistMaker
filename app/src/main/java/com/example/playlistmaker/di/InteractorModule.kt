package com.example.playlistmaker.di

import android.os.Build.VERSION_CODES.P
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import com.example.playlistmaker.Domain.db.PlaylistTrackInteractor
import com.example.playlistmaker.Domain.db.impl.FavoritesInteractorImpl
import com.example.playlistmaker.Domain.db.impl.PlaylistInteractorImpl
import com.example.playlistmaker.Domain.db.impl.PlaylistTrackInteractorImpl
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.Domain.settings.SettingsInteractor
import com.example.playlistmaker.Domain.settings.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<TrackInteractor> {
        TrackInteractorImpl(get(), get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }

    single<PlaylistTrackInteractor> {
        PlaylistTrackInteractorImpl(get())
    }
}