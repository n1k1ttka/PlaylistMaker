package com.example.playlistmaker.data.dto

data class ITunesResponce(
    val resultCount: Int,
    val results: List<TrackDto>
): Responce()