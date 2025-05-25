package com.example.playlistmaker.Data.search.impl

import android.util.Log
import com.example.playlistmaker.Data.dto.ITunesRequest
import com.example.playlistmaker.Data.dto.ITunesResponce
import com.example.playlistmaker.Data.network.NetworkClient
import com.example.playlistmaker.Domain.Resource
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val trackNetworkClient: NetworkClient
): TrackRepository {
    override fun getTracks(text: String): Flow<Resource<List<Track>>> = flow {
        val responce = trackNetworkClient.load(ITunesRequest(text))
        when (responce.resultCode) {
            400 -> emit(Resource.Error("Неверный запрос, сообщите об ошибке в поддержку"))
            500 -> emit(Resource.Error("Проверьте подключение к интернету"))
            200 -> {
                with(responce as ITunesResponce) {
                    val data = results.map {
                        Track(
                            it.previewUrl,
                            it.trackId,
                            it.trackName,
                            it.collectionName,
                            it.artistName,
                            it.country,
                            it.primaryGenreName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.releaseDate
                        )
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> emit(Resource.Error("Ошибка сервера"))
        }
    }
}