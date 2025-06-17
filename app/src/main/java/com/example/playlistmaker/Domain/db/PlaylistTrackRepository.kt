package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow


interface PlaylistTrackRepository {

    suspend fun getTracksForPlaylist(playlistId: Int): List<Track>

    suspend fun insertTrackToPlaylist(track: TrackDto, playlistId: Int): Long

    suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int)
}