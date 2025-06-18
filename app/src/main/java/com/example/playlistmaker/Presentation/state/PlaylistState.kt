package com.example.playlistmaker.Presentation.state

import com.example.playlistmaker.Domain.Playlist

sealed class PlaylistState(val playlists: List<Playlist> = emptyList(), val selectedPlaylist: Playlist? = null, val addedInPlaylist: Boolean) {

    class WebPlaylists(playlists: List<Playlist>): PlaylistState(playlists = playlists, addedInPlaylist = false)
    class PlaylistClick(selectedPlaylist: Playlist, added: Boolean): PlaylistState(selectedPlaylist = selectedPlaylist, addedInPlaylist = added)
}