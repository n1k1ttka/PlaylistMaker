package com.example.playlistmaker.Domain.search

import com.example.playlistmaker.Domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun loadTracks(text: String): Flow<Pair<List<Track>?, String?>>

    fun saveListenedTracks(tracks: List<Track>)

    fun loadListenedTracks(): List<Track>
}