package com.example.playlistmaker.Data.network

import com.example.playlistmaker.Data.dto.ITunesRequest
import com.example.playlistmaker.Data.dto.Responce

class TrackNetworkClient(
    private val iTunesService: ITunesApiService
) : NetworkClient {

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