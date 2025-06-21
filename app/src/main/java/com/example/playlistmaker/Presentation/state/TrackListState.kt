package com.example.playlistmaker.Presentation.state

import com.example.playlistmaker.Presentation.model.ParcelableTrack

sealed class TrackListState {
    object Loading: TrackListState()
    object ZeroContent: TrackListState()
    data class Content(
        val tracks: List<ParcelableTrack>
    ): TrackListState()
    data class Story(
        val story: List<ParcelableTrack>
    ): TrackListState()
    data class Error(
        val errorMessage: String
    ): TrackListState()
}