package com.example.playlistmaker.Data

import com.example.playlistmaker.Domain.Track

data class ITunesResponce(
    val resultCount: Int,
    val results: List<TrackDto>
)