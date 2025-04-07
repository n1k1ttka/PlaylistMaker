package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.Data.network.ITunesApiService
import com.example.playlistmaker.Data.network.NetworkClient
import com.example.playlistmaker.Data.network.TrackNetworkClient
import com.example.playlistmaker.Data.search.TrackManager
import com.example.playlistmaker.Data.settings.SettingsManager
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        TrackNetworkClient(get())
    }

    factory { Gson() }

    single<SharedPreferences>(named("track_prefs")) {
        androidContext()
            .getSharedPreferences("track_prefs", Context.MODE_PRIVATE)
    }

    single {
        TrackManager(get(named("track_prefs")), get())
    }

    single<SharedPreferences>(named("settings_prefs")) {
        androidContext()
            .getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
    }

    single {
        SettingsManager(get(named("settings_prefs")))
    }
}