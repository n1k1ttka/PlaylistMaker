package com.example.playlistmaker.Data.network

import com.example.playlistmaker.Data.NetworkClient
import com.example.playlistmaker.Data.dto.ITunesRequest
import com.example.playlistmaker.Data.dto.Responce
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrackNetworkClient : NetworkClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApiService::class.java)

    override fun load(dto: Any): Responce {
        if (dto is ITunesRequest) {
            val resp = iTunesService.search(dto.text).execute()

            val body = resp.body() ?: Responce()

            return body.apply { resultCode = resp.code() }
        } else {
            return Responce().apply { resultCode = 400 }
        }
    }
}