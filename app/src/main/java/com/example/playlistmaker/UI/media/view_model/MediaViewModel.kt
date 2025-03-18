package com.example.playlistmaker.UI.media.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Presentation.state.PlayerState

class MediaViewModel(
    private val mediaPlayer: MediaPlayer,
    private val mainThreadHandler: Handler
) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    val playerState: LiveData<PlayerState> = _playerState

    private var updateTimerTask: Runnable? = null
    private var isTimerRunning = false
    private var currentPosition: Int = 0
    private var isPlaying: Boolean = false
    private var rotatePlaying: Boolean = false
    private var secondsCount = 30000L
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
            _playerState.value = PlayerState.Default
            return
        }

        mediaPlayer.setOnPreparedListener {
            _playerState.value = PlayerState.Prepared
            restorePlayerState()
        }

        mediaPlayer.setOnCompletionListener {
            _playerState.value = PlayerState.Prepared
        }

        mediaPlayer.setOnErrorListener { _, what, extra ->
            Log.e("MediaViewModel", "MediaPlayer error: what=$what, extra=$extra")
            _playerState.value = PlayerState.Default
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
        isPlaying = false
        stopTimer()
        _playerState.value = PlayerState.Paused
    }

    fun playbackControl() {
        when (_playerState.value) {
            is PlayerState.Playing -> pausePlayer()
            PlayerState.Prepared, PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            if (updateTimerTask == null) {
                updateTimerTask = createUpdateTimerTask()
            }
            mainThreadHandler.post(updateTimerTask!!)
        }
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false
            updateTimerTask?.let {
                mainThreadHandler.removeCallbacks(it)
            }
        }
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

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                mediaPlayer.let { player ->
                    val currentPosition = player.currentPosition
                    val timeLeft = (secondsCount - currentPosition) / 1000
                    if (isTimerRunning && player.isPlaying && timeLeft > 0) {
                        _playerState.value = PlayerState.Playing(
                            String.format(
                                "%d:%02d",
                                timeLeft / 60,
                                timeLeft % 60
                            )
                        )
                        mainThreadHandler?.postDelayed(this, 1000)
                    } else {
                        _playerState.value = PlayerState.Playing("0:00")
                        isTimerRunning = false
                        pausePlayer()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}