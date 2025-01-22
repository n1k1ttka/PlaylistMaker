package com.example.playlistmaker.Domain

import android.content.Context
import com.example.playlistmaker.Data.HistoryRepositoryImpl
import com.example.playlistmaker.Data.SettingsManager
import com.example.playlistmaker.Data.SettingsRepositoryImpl
import com.example.playlistmaker.Data.TrackManager
import com.example.playlistmaker.Data.TrackNetworkClient
import com.example.playlistmaker.Data.TrackRepositoryImpl

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

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(getTrackNetworkClient(), getTrackManager(context))
    }

    private fun getHistoryRepository(context: Context): HistoryRepository {
        return HistoryRepositoryImpl(getTrackManager(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(getSettingsManager(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context), getHistoryRepository(context))
    }

    fun provideThemeInteractor(context: Context): ThemeInteractor {
        return ThemeInteractorImpl(getSettingsRepository(context))
    }
}