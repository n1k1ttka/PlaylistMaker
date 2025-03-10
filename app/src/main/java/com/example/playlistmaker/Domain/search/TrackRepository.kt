package com.example.playlistmaker.Domain.search

import com.example.playlistmaker.Domain.Track

interface TrackRepository {
    fun getTracks(text: String): List<Track>
}