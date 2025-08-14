package com.example.playlistmaker.services.music

import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.state.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl {
    fun preparePlayer(track: ParcelableTrack)
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun notificationOff()
    fun notificationOn()
    fun delete()
}