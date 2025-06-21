package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.PlaylistRepository
import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.PlaylistDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter
): PlaylistRepository {
    override fun getPlaylists(): Flow<List<PlaylistDto>> {
        return appDatabase.playlistsDao().getPlaylists().map { playlistEntities ->
            playlistEntities.map { dbConverter.mapPlaylist(it) }
        }
    }

    override suspend fun addPlaylist(playlist: PlaylistDto): Long {
        return appDatabase.playlistsDao().addPlaylist(dbConverter.mapPlaylist(playlist))
    }

    override suspend fun deletePlaylist(playlist: PlaylistDto) {
        appDatabase.playlistsDao().deletePlaylist(dbConverter.mapPlaylist(playlist))
    }
}