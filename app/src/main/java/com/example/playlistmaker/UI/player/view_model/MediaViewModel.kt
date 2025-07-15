package com.example.playlistmaker.UI.player.view_model

import android.media.MediaPlayer
import android.util.Log
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
import com.example.playlistmaker.Presentation.utils.SingleEventLiveData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistInteractor,
    private val playlistTrackInteractor: PlaylistTrackInteractor
) : ViewModel() {

    private var isClickAllowed = true
    private var clickJob: Job? = null
    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerState: LiveData<PlayerState> = _playerState

    private val playlistState = MutableLiveData<PlaylistState>()
    fun getPlaylistState(): LiveData<PlaylistState> = playlistState

    private val likeState = MutableLiveData(false)
    fun getLikeState(): LiveData<Boolean> = likeState

    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false
    private var rotatePlaying: Boolean = false
    private var currentTrackUrl: String? = null
    private var currentTrack: ParcelableTrack? = null

    fun preparePlayer(track: ParcelableTrack) {
        checkFavorites(track)
        if (track.previewUrl == currentTrackUrl) {
            restorePlayerState()
            return
        }

        currentTrackUrl = track.previewUrl
        currentTrack = track

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            Log.e("MediaViewModel", "Error setting data source: ${e.message}")
            _playerState.value = PlayerState.Default()
            return
        }

        mediaPlayer.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared()
        }

        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            _playerState.postValue(PlayerState.Prepared())
        }

        mediaPlayer.setOnErrorListener { _, what, extra ->
            Log.e("MediaViewModel", "MediaPlayer error: what=$what, extra=$extra")
            _playerState.value = PlayerState.Default()
            timerJob?.cancel()
            true
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        isPlaying = true
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    fun playbackControl() {
        when(playerState.value) {
            is PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, is PlayerState.Paused -> {
                startPlayer()
            }
            else -> { }
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(100L)
                _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition) ?: "00:00"
    }

    fun savePlayerState() {
        mediaPlayer.let {
            currentPosition = it.currentPosition
            rotatePlaying = it.isPlaying
        }
    }

    fun restorePlayerState() {
        mediaPlayer?.let {
            it.seekTo(currentPosition)
            if (rotatePlaying) {
                startPlayer()
            }
        }
    }

    fun releasePlayer() {
        mediaPlayer.release()
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
                Log.d("CheckFavorTracks","${favoritesInteractor.addFavorite(track.toDomain())}")
                likeState.postValue(false)
                favoritesInteractor.deleteFromFavorites(track.toDomain())
            } else {
                Log.d("CheckFavorTracks","${favoritesInteractor.addFavorite(track.toDomain())}")
                likeState.postValue(true)
            }
        }
    }

    fun getPlaylists(){
        viewModelScope.launch {
            playlistsInteractor.getPlaylists().collect() { playlists ->
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

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}