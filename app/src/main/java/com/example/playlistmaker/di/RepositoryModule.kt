package com.example.playlistmaker.di

import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.impl.FavoritesRepositoryImpl
import com.example.playlistmaker.data.search.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.Domain.db.FavoritesRepository
import com.example.playlistmaker.Domain.db.PlaylistRepository
import com.example.playlistmaker.Domain.db.PlaylistTrackRepository
import com.example.playlistmaker.Domain.search.HistoryRepository
import com.example.playlistmaker.Domain.search.TrackRepository
import com.example.playlistmaker.Domain.settings.SettingsRepository
import com.example.playlistmaker.data.db.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.data.db.impl.PlaylistTrackRepositoryImpl
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

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }

    single<PlaylistTrackRepository> {
        PlaylistTrackRepositoryImpl(get(), get())
    }

    factory { DbConverter() }
}