package com.example.playlistmaker.UI.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import com.example.playlistmaker.Domain.db.PlaylistTrackInteractor
import com.example.playlistmaker.Presentation.mappers.toDomain
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.state.PlayerState
import com.example.playlistmaker.Presentation.state.PlaylistState
import com.example.playlistmaker.services.music.AudioPlayerControl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MediaViewModel(
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistInteractor,
    private val playlistTrackInteractor: PlaylistTrackInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var playerStateJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerState: LiveData<PlayerState> = _playerState

    private var audioPlayerControl: AudioPlayerControl? = null

    private val playlistState = MutableLiveData<PlaylistState>()
    fun getPlaylistState(): LiveData<PlaylistState> = playlistState

    private val likeState = MutableLiveData(false)
    fun getLikeState(): LiveData<Boolean> = likeState

    private var currentTrack: ParcelableTrack? = null
    private var pendingTrack: ParcelableTrack? = null

    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        if (this.audioPlayerControl == audioPlayerControl) {
            audioPlayerControl.notificationOff()
            return
        }

        this.audioPlayerControl = audioPlayerControl

        pendingTrack?.let { track ->
            preparePlayer(track)
        }

        playerStateJob?.cancel()
        playerStateJob = viewModelScope.launch {
            audioPlayerControl.getPlayerState().collect {
                _playerState.postValue(it)
            }
        }
    }

    fun preparePlayer(track: ParcelableTrack) {
        if (audioPlayerControl == null) {
            pendingTrack = track
            return
        }

        pendingTrack = null
        checkFavorites(track)
        currentTrack = track
        audioPlayerControl?.preparePlayer(track)
    }

    fun playbackControl() {
        when(playerState.value) {
            is PlayerState.Playing -> {
                audioPlayerControl?.pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                audioPlayerControl?.startPlayer()
            }
            else -> {}
        }
    }

    private fun checkFavorites(track: ParcelableTrack) {
        viewModelScope.launch {
            favoritesInteractor.favoritesTracks().collect { tracks ->
                likeState.postValue(tracks.contains(track.toDomain()))
            }
        }
    }

    fun like(track: ParcelableTrack) {
        viewModelScope.launch {
            if (favoritesInteractor.addFavorite(track.toDomain()) == -1L) {
                likeState.postValue(false)
                favoritesInteractor.deleteFromFavorites(track.toDomain())
            } else {
                likeState.postValue(true)
            }
        }
    }

    fun getPlaylists(){
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect { playlists ->
                playlistState.postValue(PlaylistState.WebPlaylists(playlists))
            }
        }
    }

    fun onPlaylistClick(playlist: Playlist) {
        if (clickDebounce()) {
            viewModelScope.launch {
                currentTrack?.let {
                    playlistState.postValue(PlaylistState.PlaylistClick(playlist, playlistTrackInteractor.insertTrackToPlaylist(currentTrack!!.toDomain(), playlist.id) != -1L))
                }
            }
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

    fun notificationOn(){
        audioPlayerControl?.notificationOn()
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl?.delete()
        audioPlayerControl = null
        playerStateJob?.cancel()
        playerStateJob = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl?.releasePlayer()
        removeAudioPlayerControl()
    }
}