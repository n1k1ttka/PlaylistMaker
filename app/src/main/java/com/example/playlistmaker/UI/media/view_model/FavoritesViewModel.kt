package com.example.playlistmaker.UI.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private val trackClickEvent = SingleEventLiveData<Track>()
    fun getTrackClickEvent(): LiveData<Track> = trackClickEvent

    private val favoritesState = MutableLiveData<List<Track>>()
    fun getFavoritesState(): LiveData<List<Track>> = favoritesState

    private var isClickAllowed = true
    private var clickJob: Job? = null

    fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            trackClickEvent.setValue(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            clickJob = viewModelScope.launch {
                delay(2000L)
                isClickAllowed = true
            }
        }
        return current
    }

    fun render(){
        viewModelScope.launch {
            favoritesInteractor.favoritesTracks().collect { tracks ->
                favoritesState.postValue(tracks)
            }
        }
    }
}