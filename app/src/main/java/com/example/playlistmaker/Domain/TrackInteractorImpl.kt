package com.example.playlistmaker.Domain

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val historyRepository: HistoryRepository
): TrackInteractor {
    override fun loadTracks(text: String, consumer: TrackInteractor.TracksConsumer) {
        trackRepository.getTracks(text) { result ->
            result.onSuccess { consumer.consume(it, "") }
                .onFailure { consumer.consume(null, it.message.toString()) }
        }
    }

    override fun saveListenedTracks(tracks: List<Track>) {
        historyRepository.saveHistory(tracks)
    }

    override fun loadListenedTracks(): List<Track> {
        return historyRepository.readHistory()
    }
}