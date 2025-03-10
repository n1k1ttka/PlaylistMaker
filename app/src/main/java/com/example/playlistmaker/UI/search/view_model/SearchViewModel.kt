package com.example.playlistmaker.UI.search.view_model


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.state.TrackListState
import com.example.playlistmaker.Domain.search.TrackInteractor
import com.example.playlistmaker.Presentation.model.STORYSIZE
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import com.example.playlistmaker.creator.Creator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application
): AndroidViewModel(application) {

    private val trackInteractor = Creator.provideTrackInteractor(getApplication())

    private val state = MutableLiveData<TrackListState>()
    fun getState(): LiveData<TrackListState> = state

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun getTrackClickEvent(): LiveData<Track> = trackClickEvent

    private val trackListenedClickEvent = SingleEventLiveData<Track>()
    fun getListenedTrackClickEvent(): LiveData<Track> = trackListenedClickEvent

    private var isClickAllowed = true

    private var searchJob: Job? = null
    private var clickJob: Job? = null
    private var latestSearchText: String? = null

    private val _songs = mutableListOf<Track>()
    val songs: List<Track> = _songs
    private val _story = mutableListOf<Track>()
    val story: List<Track> = _story

    fun loadListenedTracks(){
        state.value = TrackListState.Loading
        _story.clear()
        _story.addAll(trackInteractor.loadListenedTracks())
        state.value = TrackListState.Story(story)
    }

    fun saveListenedTracks(){
        trackInteractor.saveListenedTracks(story)
    }

    fun clearListenedTracks(){
        _story.clear()
        _songs.clear()
        saveListenedTracks()
    }

    fun clickDebounce() : Boolean {
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
        } else if (changedText.isNullOrEmpty())
            loadListenedTracks()
        else {
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

    fun addInStory(item: Track){
        var inStory = false
        if (_story.contains(item)) {
            _story.remove(item)
            _story.add(0, item)
            inStory = true
        }
        if (!inStory) {
            if (_story.size < STORYSIZE) {
               _story.add(0, item)
            } else {
                _story.removeAt(9)
                _story.add(0, item)
            }
        }
        saveListenedTracks()
    }

    fun onTrackClick(track: Track) {
        if (clickDebounce()){
            addInStory(track)
            trackClickEvent.setValue(track)
        }
    }
    fun onListenedTrackClick(track: Track) {
        if (clickDebounce()){
            trackListenedClickEvent.setValue(track)
        }
    }

    fun remoteRequest(inputEditText: String){
        state.postValue(TrackListState.Loading)
        trackInteractor.loadTracks(inputEditText, object : TrackInteractor.TracksConsumer {
            override fun consume(tracks: List<Track>?) {
                if (tracks != null) {
                    if (tracks.isNotEmpty()) {
                        _songs.clear()
                        _songs.addAll(tracks)
                        state.postValue(TrackListState.Content(tracks))
                    } else state.postValue(TrackListState.ZeroContent)
                } else state.postValue(TrackListState.Error("Проверьте подключение к сети"))
            }

        })
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}