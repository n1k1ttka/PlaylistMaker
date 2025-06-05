package com.example.playlistmaker.Domain.db.impl

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Domain.db.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val favoritesRepository: FavoritesRepository
): FavoritesInteractor {
    override fun favoritesTracks(): Flow<List<Track>> {
        return favoritesRepository.favoritesTracks()
    }

    override suspend fun addFavorite(track: Track): Long {
        return favoritesRepository.addFavorite(
            TrackDto(
                track.previewUrl,
                track.trackId,
                track.trackName,
                track.collectionName,
                track.artistName,
                track.country,
                track.primaryGenreName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.releaseDate
            )
        )
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromFavorites(
            TrackDto(
                track.previewUrl,
                track.trackId,
                track.trackName,
                track.collectionName,
                track.artistName,
                track.country,
                track.primaryGenreName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.releaseDate
            )
        )
    }
}