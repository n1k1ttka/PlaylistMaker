package com.example.playlistmaker.Presentation.state

sealed class PlayerState {
    object Default : PlayerState()
    object Prepared : PlayerState()
    data class Playing(val timeLeft: String) : PlayerState()
    object Paused : PlayerState()
}