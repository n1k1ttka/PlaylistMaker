package com.example.playlistmaker.Domain.db

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.data.dto.FavoriteDto
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun favoritesTracks(): Flow<List<Track>>

    suspend fun addFavorite(track: FavoriteDto): Long

    suspend fun deleteFromFavorites(track: FavoriteDto)
}