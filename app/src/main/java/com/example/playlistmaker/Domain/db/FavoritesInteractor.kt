package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.Domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {

    fun favoritesTracks(): Flow<List<Track>>

    suspend fun addFavorite(track: Track): Long

    suspend fun deleteFromFavorites(track: Track)
}