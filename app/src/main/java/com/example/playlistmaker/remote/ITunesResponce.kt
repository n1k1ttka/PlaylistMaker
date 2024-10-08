package com.example.playlistmaker.remote

import com.example.playlistmaker.model.Track

data class ITunesResponce(
    val resultCount: Int,
    val results: List<Track>
)