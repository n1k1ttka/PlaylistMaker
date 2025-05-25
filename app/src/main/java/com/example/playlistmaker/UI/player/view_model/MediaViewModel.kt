package com.example.playlistmaker.UI.player.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Presentation.state.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel(
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    private var timerJob: Job? = null

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    val playerState: LiveData<PlayerState> = _playerState

    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false
    private var rotatePlaying: Boolean = false
    private var currentTrackUrl: String? = null

    fun preparePlayer(url: String) {
        if (url == currentTrackUrl) {
            restorePlayerState()
            return
        }

        currentTrackUrl = url

        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
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
            _playerState.value = PlayerState.Prepared()
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
                delay(300L)
                _playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
            pausePlayer()
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

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}