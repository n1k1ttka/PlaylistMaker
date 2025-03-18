package com.example.playlistmaker.Domain.search

import com.example.playlistmaker.Domain.Track

interface HistoryRepository {
    fun readHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}