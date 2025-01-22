package com.example.playlistmaker.Domain

import retrofit2.Callback

interface TrackRepository {
    fun getTracks(text: String, callback: (Result<List<Track>>) -> Unit)
}