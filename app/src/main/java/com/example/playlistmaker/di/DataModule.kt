package com.example.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.network.ITunesApiService
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.TrackNetworkClient
import com.example.playlistmaker.data.search.TrackManager
import com.example.playlistmaker.data.settings.SettingsManager
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

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").fallbackToDestructiveMigration().build()
    }
}