package com.example.playlistmaker.Data.dto

data class ITunesResponce(
    val resultCount: Int,
    val results: List<TrackDto>
): Responce()