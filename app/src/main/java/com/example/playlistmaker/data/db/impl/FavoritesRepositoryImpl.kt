package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.TrackDbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
): FavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> {
        return appDatabase.favoritesDao().getFavorites().map { trackEntities ->
            trackEntities.map { trackDbConverter.map(it) }
        }
    }

    override suspend fun addFavorite(track: TrackDto): Long {
        return appDatabase.favoritesDao().insertTracks(trackDbConverter.map(track))
    }

    override suspend fun deleteFromFavorites(track: TrackDto) {
        appDatabase.favoritesDao().deleteFromFavorites(trackDbConverter.map(track))
    }
}