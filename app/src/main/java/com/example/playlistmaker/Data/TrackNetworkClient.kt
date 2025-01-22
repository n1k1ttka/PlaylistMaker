package com.example.playlistmaker.Data

import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackNetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApiService::class.java)

    fun loadTracks(text: String, callback: Callback<ITunesResponce>) {
        iTunesService.search(text).enqueue(callback)
    }
}