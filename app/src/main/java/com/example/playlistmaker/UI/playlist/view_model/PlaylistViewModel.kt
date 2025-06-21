package com.example.playlistmaker.UI.playlist.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import com.example.playlistmaker.Domain.db.PlaylistTrackInteractor
import com.example.playlistmaker.Presentation.mappers.toParcelable
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.state.PlaylistTrackState
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import com.example.playlistmaker.Presentation.utils.convertMillisToMinutesAndSeconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistTrackInteractor: PlaylistTrackInteractor,
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val playlistState = MutableLiveData<PlaylistTrackState>()
    fun getPlaylistState(): LiveData<PlaylistTrackState> = playlistState

    private val trackClickEvent = SingleEventLiveData<ParcelableTrack>()
    fun getTrackClickEvent(): LiveData<ParcelableTrack> = trackClickEvent

    private val longTrackClickEvent = SingleEventLiveData<ParcelableTrack>()
    fun getLongTrackClickEvent(): LiveData<ParcelableTrack> = longTrackClickEvent

    private var isClickAllowed = true
    private var clickJob: Job? = null


    fun render(playlistId: Int){
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylist(playlistId)
            val tracks = playlistTrackInteractor.getTracksForPlaylist(playlistId).map { track ->  track.toParcelable() }
            playlistState.postValue(PlaylistTrackState(playlist, tracks))
        }
    }

    fun onTrackClick(track: ParcelableTrack) {
        if (clickDebounce()) {
            trackClickEvent.setValue(track)
        }
    }

    fun onLongTrackClick(track: ParcelableTrack) {
        longTrackClickEvent.setValue(track)
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

    fun deleteTrack(track: ParcelableTrack, currentPlaylist: Playlist){
        viewModelScope.launch {
            playlistTrackInteractor.deleteTrackFromPlaylist(currentPlaylist.id, track.trackId)
            val playlist = playlistInteractor.getPlaylist(currentPlaylist.id)
            val tracks = playlistTrackInteractor.getTracksForPlaylist(playlist.id).map { track ->  track.toParcelable() }
            playlistState.postValue(PlaylistTrackState(playlist, tracks))
        }
    }

    fun deletePlaylist(){
        viewModelScope.launch {
            playlistState.value.let {
                playlistInteractor.deletePlaylist(playlistState.value!!.playlist)
            }
        }
    }

    fun generatePlaylistMessage(playlist: Playlist?, tracks: List<ParcelableTrack>?): String {

        val builder = StringBuilder()
        builder.appendLine(playlist?.playlistName)
        builder.appendLine(playlist?.description)
        builder.appendLine("${tracks?.size} треков")
        builder.appendLine()

        tracks?.forEachIndexed { index, track ->
            val duration = convertMillisToMinutesAndSeconds(track.trackTimeMillis)
            builder.appendLine("${index + 1}. ${track.artistName} - ${track.trackName} ($duration)")
        }

        return builder.toString()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}