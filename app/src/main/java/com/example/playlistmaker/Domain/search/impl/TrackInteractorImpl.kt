package com.example.playlistmaker.Domain.search.impl

import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.search.HistoryRepository
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Domain.search.TrackRepository
import java.io.IOException
import java.util.concurrent.Executors

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: HistoryRepository
): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun loadTracks(text: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            try {
                consumer.consume(trackRepository.getTracks(text))
            } catch (e: IOException) {
                consumer.consume(null)
            }
        }
    }

    override fun saveListenedTracks(tracks: List<Track>) {
        historyRepository.saveHistory(tracks)
    }

    override fun loadListenedTracks(): List<Track> {
        return historyRepository.readHistory()
    }
}