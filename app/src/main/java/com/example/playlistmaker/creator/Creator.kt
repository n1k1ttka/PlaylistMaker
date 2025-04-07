package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
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

//object Creator {
//
//    private lateinit var applicationContext: Context
//
//    fun init(context: Context) {
//        applicationContext = context.applicationContext
//    }
//
//    fun provideTrackInteractor(): TrackInteractor {
//        return TrackInteractorImpl(getTrackRepository(), getHistoryRepository())
//    }
//
//    fun provideThemeInteractor(): SettingsInteractor {
//        return SettingsInteractorImpl(getSettingsRepository())
//    }
//
//    private fun getTrackNetworkClient(): TrackNetworkClient {
//        return TrackNetworkClient()
//    }
//
//    private fun getTrackManager(): TrackManager {
//        return TrackManager(getTrackPreferences())
//    }
//
//    private fun getSettingsManager(): SettingsManager {
//        return SettingsManager(getSettingsPreferences())
//    }
//
//    private fun getTrackRepository(): TrackRepository {
//        return TrackRepositoryImpl(getTrackNetworkClient())
//    }
//
//    private fun getHistoryRepository(): HistoryRepository {
//        return HistoryRepositoryImpl(getTrackManager())
//    }
//
//    private fun getSettingsRepository(): SettingsRepository {
//        return SettingsRepositoryImpl(getSettingsManager())
//    }
//
//    private fun getTrackPreferences(): SharedPreferences {
//        return applicationContext.getSharedPreferences("track_prefs", Context.MODE_PRIVATE)
//    }
//
//    private fun getSettingsPreferences(): SharedPreferences {
//        return applicationContext.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
//    }
//}