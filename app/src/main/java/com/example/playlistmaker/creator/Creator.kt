package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.Data.search.impl.HistoryRepositoryImpl
import com.example.playlistmaker.Data.settings.SettingsManager
import com.example.playlistmaker.Data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.Data.search.TrackManager
import com.example.playlistmaker.Data.network.TrackNetworkClient
import com.example.playlistmaker.Data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Domain.search.TrackRepository
import com.example.playlistmaker.Domain.search.HistoryRepository
import com.example.playlistmaker.Domain.search.impl.TrackInteractorImpl
import com.example.playlistmaker.Domain.settings.SettingsRepository
import com.example.playlistmaker.Domain.settings.SettingsInteractor
import com.example.playlistmaker.Domain.settings.impl.SettingsInteractorImpl

object Creator {

    private fun getTrackNetworkClient(): TrackNetworkClient {
        return TrackNetworkClient()
    }

    private fun getTrackManager(context: Context): TrackManager {
        return TrackManager(context)
    }

    private fun getSettingsManager(context: Context): SettingsManager {
        return SettingsManager(context)
    }

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(getTrackNetworkClient())
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(getTrackManager(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsManager(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(), getHistoryRepository(context))
    }

    fun provideThemeInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}