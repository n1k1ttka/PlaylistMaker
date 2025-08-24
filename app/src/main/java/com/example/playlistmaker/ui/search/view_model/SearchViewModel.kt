package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Presentation.state.TrackListState
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Presentation.mappers.toDomain
import com.example.playlistmaker.Presentation.mappers.toParcelable
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.model.search.STORYSIZE
import com.example.playlistmaker.Presentation.utils.SingleEventFlow
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<TrackListState>(TrackListState.Content(emptyList()))
    val state: StateFlow<TrackListState> = _state

    private val trackClickEvent = SingleEventLiveData<ParcelableTrack>()
    fun getTrackClickEvent(): LiveData<ParcelableTrack> = trackClickEvent

    private val trackListenedClickEvent = SingleEventFlow<ParcelableTrack>()
    fun getListenedTrackClickEvent(): SharedFlow<ParcelableTrack> = trackListenedClickEvent.events

    private var isClickAllowed = true
    private var searchJob: Job? = null
    private var clickJob: Job? = null
    private var latestSearchText: String? = null

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun setQuery(value: String) { _query.value = value }

    private val _songs = mutableListOf<ParcelableTrack>()
    private val _story = mutableListOf<ParcelableTrack>()

    fun loadListenedTracks() {
        _story.clear()
        _story.addAll(trackInteractor.loadListenedTracks().map { it.toParcelable() })
        if (_query.value.isEmpty()) {
            _state.value = TrackListState.Story(_story)
        } else if (_songs.isNotEmpty()) {
            _state.value = TrackListState.Content(_songs)
        }
    }

    fun saveListenedTracks() {
        trackInteractor.saveListenedTracks(_story.map { it.toDomain() })
    }

    fun clearListenedTracks() {
        _story.clear()
        saveListenedTracks()
        _state.value = TrackListState.Content(emptyList())
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob = viewModelScope.launch {
                delay(200L)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(changedText: String) {
        if (changedText == latestSearchText) return
        _query.value = changedText
        latestSearchText = changedText
        cancelSearch()
        if (changedText.isEmpty()) {
            loadListenedTracks()
        } else {
            searchJob = viewModelScope.launch {
                delay(2000L)
                remoteRequest(changedText)
            }
        }
    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    fun addInStory(item: ParcelableTrack) {
        if (_story.remove(item)) {
            _story.add(0, item)
        } else {
            if (_story.size >= STORYSIZE) {
                _story.removeAt(_story.size - 1)
            }
            _story.add(0, item)
        }
        saveListenedTracks()
    }

    fun onTrackClick(track: ParcelableTrack) {
        if (clickDebounce()) {
            trackClickEvent.setValue(track)
            addInStory(track)
        }
    }

    fun triggerEvent(track: ParcelableTrack) {
        viewModelScope.launch { trackListenedClickEvent.sendEvent(track) }
    }

    fun remoteRequest(inputEditText: String) {
        _state.value = TrackListState.Loading
        viewModelScope.launch {
            trackInteractor.loadTracks(inputEditText)
                .collect { pair ->
                    val tracks = pair.first
                    val error = pair.second
                    when {
                        error != null -> _state.value = TrackListState.Error(error.toString())
                        tracks != null && tracks.isNotEmpty() -> {
                            _songs.clear()
                            _songs.addAll(tracks.map { it.toParcelable() })
                            _state.value = TrackListState.Content(_songs)
                        }
                        else -> _state.value = TrackListState.ZeroContent
                    }
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}