package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.Domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistTrackInteractor {

    suspend fun getTracksForPlaylist(playlistId: Int): List<Track>

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Int): Long

    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)
}