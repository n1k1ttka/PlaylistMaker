package com.example.playlistmaker.data.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.Domain.Track

class TrackDbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.previewUrl, track.trackId, track.trackName, track.collectionName, track.artistName, track.country, track.primaryGenreName, track.trackTimeMillis, track.artworkUrl100, track.releaseDate)
    }
}