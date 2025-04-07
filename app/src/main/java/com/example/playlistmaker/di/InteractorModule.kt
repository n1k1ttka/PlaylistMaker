package com.example.playlistmaker.di

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
}