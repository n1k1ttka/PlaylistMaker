package com.example.playlistmaker.Data.search.impl

import com.example.playlistmaker.Data.dto.ITunesRequest
import com.example.playlistmaker.Data.dto.ITunesResponce
import com.example.playlistmaker.Data.network.TrackNetworkClient
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.search.TrackRepository

class TrackRepositoryImpl(
    private val trackNetworkClient: TrackNetworkClient
): TrackRepository {
    override fun getTracks(text: String): List<Track> {
        val responce = trackNetworkClient.load(ITunesRequest(text))
        if (responce.resultCode == 200) {
            return (responce as ITunesResponce).results.map {
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
                    it.releaseDate)
            }
        } else {
            return emptyList()
        }
    }

}