package com.example.playlistmaker.Domain.db.impl

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.PlaylistTrackInteractor
import com.example.playlistmaker.Domain.db.PlaylistTrackRepository
import com.example.playlistmaker.data.dto.TrackDto

class PlaylistTrackInteractorImpl(
    private val playlistTrackRepository: PlaylistTrackRepository
): PlaylistTrackInteractor {
    override suspend fun getTracksForPlaylist(playlistId: Int): List<Track> {
        return playlistTrackRepository.getTracksForPlaylist(playlistId)
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Int): Long {
        return playlistTrackRepository.insertTrackToPlaylist(
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
            ),
            playlistId
        )
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        playlistTrackRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }
}