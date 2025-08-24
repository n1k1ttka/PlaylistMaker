package com.example.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayListsViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val playlistState = MutableLiveData<List<Playlist>>()
    fun getPlaylistState(): LiveData<List<Playlist>> = playlistState

    private val playlistClickEvent = SingleEventLiveData<Playlist>()
    fun getPlaylistClickEvent(): LiveData<Playlist> = playlistClickEvent

    private var isClickAllowed = true
    private var clickJob: Job? = null

    fun onPlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            playlistClickEvent.setValue(playlist)
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
            playlistInteractor.getPlaylists().collect{ playlists ->
                playlistState.postValue(playlists)
            }
        }
    }
}