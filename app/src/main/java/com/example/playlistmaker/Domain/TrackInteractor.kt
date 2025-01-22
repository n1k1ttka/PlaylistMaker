package com.example.playlistmaker.Domain

interface TrackInteractor {
    fun loadTracks(text: String, consumer: TracksConsumer)

    fun saveListenedTracks(tracks: List<Track>)

    fun loadListenedTracks(): List<Track>

    interface TracksConsumer {
        fun consume(tracks: List<Track>?, comment: String)
    }
}