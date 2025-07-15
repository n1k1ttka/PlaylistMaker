package com.example.playlistmaker.Presentation.state

import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Presentation.model.ParcelableTrack

data class PlaylistTrackState(
    val playlist: Playlist,
    val tracks: List<ParcelableTrack>
)