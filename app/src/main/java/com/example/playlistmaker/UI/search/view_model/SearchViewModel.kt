package com.example.playlistmaker.UI.search.view_model

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
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor
) : ViewModel() {

    private val state = MutableLiveData<TrackListState>()
    fun getState(): LiveData<TrackListState> = state

    private val trackClickEvent = SingleEventLiveData<ParcelableTrack>()
    fun getTrackClickEvent(): LiveData<ParcelableTrack> = trackClickEvent

    private val trackListenedClickEvent = SingleEventFlow<ParcelableTrack>()
    fun getListenedTrackClickEvent(): SharedFlow<ParcelableTrack> = trackListenedClickEvent.events

    private var isClickAllowed = true

    private var searchJob: Job? = null
    private var clickJob: Job? = null
    private var latestSearchText: String? = null

    private val _songs = mutableListOf<ParcelableTrack>()
    private val _story = mutableListOf<ParcelableTrack>()

    fun loadListenedTracks() {
        state.value = TrackListState.Loading
        _story.clear()
        _story.addAll(trackInteractor.loadListenedTracks().map { listenedTrack -> listenedTrack.toParcelable() })
        state.value = TrackListState.Story(_story)
    }

    fun saveListenedTracks() {
        trackInteractor.saveListenedTracks(_story.map { it.toDomain() })
    }

    fun clearListenedTracks() {
        _story.clear()
        _songs.clear()
        saveListenedTracks()
        state.value = TrackListState.Story(_story)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(changedText: String) {
        if (changedText == latestSearchText) {
            return
        } else if (changedText.isNullOrEmpty()) {
            loadListenedTracks()
        } else {
            latestSearchText = changedText
            cancelSearch()

            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
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
        viewModelScope.launch {
            trackListenedClickEvent.sendEvent(track)
        }
    }

    fun remoteRequest(inputEditText: String) {
        state.postValue(TrackListState.Loading)

        viewModelScope.launch {
            trackInteractor.loadTracks(inputEditText)
                .collect { pair ->
                    val tracks = pair.first
                    val error = pair.second
                    if (error != null) state.postValue(TrackListState.Error(pair.second.toString()))
                    else if (tracks != null) {
                        if (tracks.isNotEmpty()) {
                            _songs.clear()
                            _songs.addAll(tracks.map { it.toParcelable() })
                            state.postValue(TrackListState.Content(_songs))
                        }
                    } else {
                        state.postValue(TrackListState.ZeroContent)
                    }
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}