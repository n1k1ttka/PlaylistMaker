package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.Domain.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(playlist: Playlist): Long

    suspend fun deletePlaylist(playlist: Playlist)
}