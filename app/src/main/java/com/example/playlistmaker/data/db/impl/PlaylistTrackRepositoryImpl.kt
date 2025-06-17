package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.PlaylistTrackRepository
import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.TrackDto

class PlaylistTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter
): PlaylistTrackRepository {
    override suspend fun getTracksForPlaylist(playlistId: Int): List<Track> {
        return appDatabase.playlistTrackDao().getTracksForPlaylist(playlistId).map { dbConverter.map(it) }
    }

    override suspend fun insertTrackToPlaylist(track: TrackDto, playlistId: Int): Long {
        return appDatabase.playlistTrackDao().insertTrackToPlaylist(dbConverter.map(track), playlistId)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlistId: Int) {
        appDatabase.playlistTrackDao().deleteTrackFromPlaylist(trackId, playlistId)
    }
}