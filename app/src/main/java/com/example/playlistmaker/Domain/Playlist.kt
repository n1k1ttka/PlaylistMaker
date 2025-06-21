package com.example.playlistmaker.Domain

data class Playlist(
    val id: Int,
    val avatarPath: String,
    val playlistName: String,
    val description: String,
    val tracksCount: Int
)