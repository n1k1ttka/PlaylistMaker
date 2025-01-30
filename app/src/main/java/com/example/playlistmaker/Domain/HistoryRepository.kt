package com.example.playlistmaker.Domain

interface HistoryRepository {
    fun readHistory(): List<Track>
    fun saveHistory(tracks: List<Track>)
}