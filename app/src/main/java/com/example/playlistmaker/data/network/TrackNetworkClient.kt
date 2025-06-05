package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.ITunesRequest
import com.example.playlistmaker.data.dto.Responce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackNetworkClient(
    private val iTunesService: ITunesApiService
) : NetworkClient {

    override suspend fun load(dto: Any): Responce {
        if (dto !is ITunesRequest) {
            return Responce().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO){
            try {
                val resp = iTunesService.search(dto.text)
                resp.apply { resultCode = 200 }
            }  catch (e: Exception) {
                Responce().apply { resultCode = 500 }
            }
        }
    }
}