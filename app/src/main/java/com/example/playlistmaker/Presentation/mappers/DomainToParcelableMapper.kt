package com.example.playlistmaker.Presentation.mappers

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.model.ParcelableTrack

fun Track.toParcelable() = ParcelableTrack(
    previewUrl, trackId, trackName, collectionName,
    artistName, country, primaryGenreName,
    trackTimeMillis, artworkUrl100, releaseDate
)

fun ParcelableTrack.toDomain() = Track(
    previewUrl, trackId, trackName, collectionName,
    artistName, country, primaryGenreName,
    trackTimeMillis, artworkUrl100, releaseDate
)