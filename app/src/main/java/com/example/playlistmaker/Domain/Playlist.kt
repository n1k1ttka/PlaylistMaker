package com.example.playlistmaker.Domain

import java.io.Serializable

data class Playlist(
    val id: Int,
    val avatarPath: String,
    val playlistName: String,
    val description: String?,
    val tracksCount: Int,
    val tracksDuration: Long
): Serializable