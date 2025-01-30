package com.example.playlistmaker.Data

import com.example.playlistmaker.Data.dto.TrackDto
import com.example.playlistmaker.Domain.HistoryRepository
import com.example.playlistmaker.Domain.Track

class HistoryRepositoryImpl(
    private val trackManager: TrackManager
): HistoryRepository {
    override fun readHistory(): List<Track> {
        return trackManager.readHistory().map{
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
    }

    override fun saveHistory(tracks: List<Track>) {
        trackManager.saveHistory(tracks.map {
            TrackDto(
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
        })
    }
}