package com.example.playlistmaker.di

import com.example.playlistmaker.Data.search.impl.HistoryRepositoryImpl
import com.example.playlistmaker.Data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.Data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.Domain.search.HistoryRepository
import com.example.playlistmaker.Domain.search.TrackRepository
import com.example.playlistmaker.Domain.settings.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<HistoryRepository> {
        HistoryRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}