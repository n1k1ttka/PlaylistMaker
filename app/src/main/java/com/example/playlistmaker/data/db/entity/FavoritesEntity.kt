package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks_table")
data class FavoritesEntity(
    val previewUrl: String,
    @PrimaryKey @ColumnInfo(name = "track_id") val trackId: Int,
    val trackName: String,
    val collectionName: String,
    val artistName: String,
    val country: String,
    val primaryGenreName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val releaseDate: String,
    @ColumnInfo(name = "added_at") val addedAt: Long = System.currentTimeMillis()
)

