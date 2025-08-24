package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Presentation.mappers.toParcelable
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
): ViewModel() {

    private val trackClickEvent = SingleEventLiveData<ParcelableTrack>()
    fun getTrackClickEvent(): LiveData<ParcelableTrack> = trackClickEvent

    private val favoritesState = MutableLiveData<List<ParcelableTrack>>()
    fun getFavoritesState(): LiveData<List<ParcelableTrack>> = favoritesState

    private var isClickAllowed = true
    private var clickJob: Job? = null

    fun onTrackClick(track: ParcelableTrack) {
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
                favoritesState.postValue(tracks.map { track -> track.toParcelable() })
            }
        }
    }
}