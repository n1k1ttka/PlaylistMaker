package com.example.playlistmaker.Presentation.state

import com.example.playlistmaker.Domain.Track

sealed class TrackListState {
    object Loading: TrackListState()
    object ZeroContent: TrackListState()
    data class Content(
        val tracks: List<Track>
    ): TrackListState()
    data class Story(
        val story: List<Track>
    ): TrackListState()
    data class Error(
        val errorMessage: String
    ): TrackListState()
}