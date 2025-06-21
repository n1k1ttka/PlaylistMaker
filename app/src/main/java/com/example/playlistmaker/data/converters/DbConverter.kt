package com.example.playlistmaker.data.converters

import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.data.db.entity.FavoritesEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.dto.FavoriteDto
import com.example.playlistmaker.data.dto.PlaylistDto

class DbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }

    fun mapFavorite(track: FavoriteDto): FavoritesEntity {
        return FavoritesEntity(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }

    fun mapFavorite(track: FavoritesEntity): Track {
        return Track(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }

    fun mapPlaylist(playlist: PlaylistDto): PlaylistEntity {
        return PlaylistEntity(playlistId = playlist.id, avatarPath = playlist.avatarPath, playlistName = playlist.playlistName, description = playlist.description)
    }

    fun mapPlaylist(playlist: PlaylistEntity): PlaylistDto {
        return PlaylistDto(id = playlist.playlistId, avatarPath = playlist.avatarPath, playlistName = playlist.playlistName, description = playlist.description)
    }
}