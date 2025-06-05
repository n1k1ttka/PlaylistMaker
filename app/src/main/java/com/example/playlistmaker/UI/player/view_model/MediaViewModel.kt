package com.example.playlistmaker.UI.player.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Domain.db.FavoritesInteractor
import com.example.playlistmaker.Presentation.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerState: LiveData<PlayerState> = _playerState

    private val likeState = MutableLiveData(false)
    fun getLikeState(): LiveData<Boolean> = likeState

    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false
    private var rotatePlaying: Boolean = false
    private var currentTrackUrl: String? = null

    fun preparePlayer(track: Track) {
        checkFavorites(track)
        if (track.previewUrl == currentTrackUrl) {
            restorePlayerState()
            return
        }

        currentTrackUrl = track.previewUrl

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

    private fun checkFavorites(track: Track) {
        viewModelScope.launch {
            favoritesInteractor.favoritesTracks().collect { tracks ->
                likeState.postValue(tracks.contains(track))
            }
        }
    }

    fun like(track: Track) {
        viewModelScope.launch {
            if (favoritesInteractor.addFavorite(track) == -1L) {
                Log.d("CheckFavorTracks","${favoritesInteractor.addFavorite(track)}")
                likeState.postValue(false)
                favoritesInteractor.deleteFromFavorites(track)
            } else {
                Log.d("CheckFavorTracks","${favoritesInteractor.addFavorite(track)}")
                likeState.postValue(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}