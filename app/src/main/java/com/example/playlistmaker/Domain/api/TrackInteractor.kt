package com.example.playlistmaker.Domain.api

import com.example.playlistmaker.Domain.Track

interface TrackInteractor {
    fun loadTracks(text: String, consumer: TracksConsumer)

    fun saveListenedTracks(tracks: List<Track>)

    fun loadListenedTracks(): List<Track>

    interface TracksConsumer {
        fun consume(tracks: List<Track>?)
    }
}