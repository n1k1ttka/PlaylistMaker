package com.example.playlistmaker.Domain.search.impl

import com.example.playlistmaker.Domain.Resource
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.search.HistoryRepository
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: HistoryRepository
): TrackInteractor {

    override fun loadTracks(text: String): Flow<Pair<List<Track>?, String?>> {
        return trackRepository.getTracks(text).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> Pair(null, result.error)
            }
        }
    }

    override fun saveListenedTracks(tracks: List<Track>) {
        historyRepository.saveHistory(tracks)
    }

    override fun loadListenedTracks(): List<Track> {
        return historyRepository.readHistory()
    }
}