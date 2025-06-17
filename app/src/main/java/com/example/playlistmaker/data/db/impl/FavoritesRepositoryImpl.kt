package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.converters.DbConverter
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesRepository
import com.example.playlistmaker.data.dto.FavoriteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConverter: DbConverter
): FavoritesRepository {
    override fun favoritesTracks(): Flow<List<Track>> {
        return appDatabase.favoritesDao().getAllFavoriteTracks().map { trackEntities ->
            trackEntities.map { dbConverter.mapFavorite(it) }
        }
    }

    override suspend fun addFavorite(track: FavoriteDto): Long {
        return appDatabase.favoritesDao().insertFavoriteTrack(dbConverter.mapFavorite(track))
    }

    override suspend fun deleteFromFavorites(track: FavoriteDto) {
        appDatabase.favoritesDao().deleteFavoriteTrack(dbConverter.mapFavorite(track))
    }
}