package com.example.playlistmaker.Data

import com.example.playlistmaker.Domain.HistoryRepository
import com.example.playlistmaker.Domain.Track

class HistoryRepositoryImpl(
    private val trackManager: TrackManager
): HistoryRepository {
    override fun readHistory(): List<Track> {
        return trackManager.readHistory()
    }

    override fun saveHistory(tracks: List<Track>) {
        trackManager.saveHistory(tracks)
    }
}