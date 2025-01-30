package com.example.playlistmaker.Domain.api

import com.example.playlistmaker.Domain.Track

interface TrackRepository {
    fun getTracks(text: String): List<Track>
}