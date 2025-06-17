package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.data.dto.PlaylistDto
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getPlaylists(): Flow<List<PlaylistDto>>

    suspend fun addPlaylist(playlist: PlaylistDto): Long

    suspend fun deletePlaylist(playlist: PlaylistDto)
}