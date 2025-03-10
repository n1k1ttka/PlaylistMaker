package com.example.playlistmaker.UI.media.view_model

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaViewModel : ViewModel() {

    sealed class PlayerState {
        object Default : PlayerState()
        object Prepared : PlayerState()
        data class Playing(val timeLeft: String) : PlayerState()
        object Paused : PlayerState()
    }

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.Default)
    val playerState: LiveData<PlayerState> = _playerState

    private var mediaPlayer: MediaPlayer? = null
    private var mainThreadHandler: Handler? = null
    private var updateTimerTask: Runnable? = null
    private var isTimerRunning = false
    private var secondsCount = 30000L

    private var mediaUrl: String = ""

    init {
        mediaPlayer = MediaPlayer()
        mainThreadHandler = Handler(Looper.getMainLooper())
    }

    fun preparePlayer(url: String) {
        mediaUrl = url
        mediaPlayer?.apply {
            setDataSource(mediaUrl)
            prepareAsync()
            setOnPreparedListener {
                _playerState.value = PlayerState.Prepared
            }
            setOnCompletionListener {
                _playerState.value = PlayerState.Prepared
            }
        }
    }

    fun startPlayer() {
        mediaPlayer?.start()
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayer?.pause()
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
            mainThreadHandler?.post(updateTimerTask!!)
        }
    }

    private fun stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false
            updateTimerTask?.let {
                mainThreadHandler?.removeCallbacks(it)
            }
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                mediaPlayer?.let { player ->
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
        mediaPlayer?.release()
        mediaPlayer = null
    }
}