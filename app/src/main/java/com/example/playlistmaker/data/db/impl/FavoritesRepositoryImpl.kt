package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): FavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.favoritesDao().getFavorites()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addFavorite(track: TrackDto): Long {
        return appDatabase.favoritesDao().insertTracks(trackDbConverter.map(track))
    }

    override suspend fun deleteFromFavorites(track: TrackDto) {
        appDatabase.favoritesDao().deleteFromFavorites(trackDbConverter.map(track))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}