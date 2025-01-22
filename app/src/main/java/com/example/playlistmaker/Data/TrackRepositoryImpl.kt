package com.example.playlistmaker.Data

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.TrackRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient,
    private val trackManager: TrackManager
): TrackRepository {
    override fun getTracks(text: String, callback: (Result<List<Track>>) -> Unit) {
        trackNetworkClient.loadTracks(text, object : Callback<ITunesResponce>{
            override fun onResponse(
                call: Call<ITunesResponce>,
                response: Response<ITunesResponce>
            ) {
                if(response.code() == 200) {
                    val songsDtos = response.body()?.results ?: emptyList()
                    val songs = songsDtos.map {
                        Track( it.previewUrl,
                            it.trackId,
                            it.trackName,
                            it.collectionName,
                            it.artistName,
                            it.country,
                            it.primaryGenreName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.releaseDate)
                    }
                    trackManager.saveHistory(songs)
                    callback(Result.success(songs))
                }
            }

            override fun onFailure(call: Call<ITunesResponce>, t: Throwable) {
                callback(Result.failure(t))
            }

        })
    }

}