package com.example.playlistmaker.Domain

import com.example.playlistmaker.Domain.api.TrackInteractor
import com.example.playlistmaker.Domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: HistoryRepository
): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun loadTracks(text: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(trackRepository.getTracks(text))
        }
    }

    override fun saveListenedTracks(tracks: List<Track>) {
        historyRepository.saveHistory(tracks)
    }

    override fun loadListenedTracks(): List<Track> {
        return historyRepository.readHistory()
    }
}