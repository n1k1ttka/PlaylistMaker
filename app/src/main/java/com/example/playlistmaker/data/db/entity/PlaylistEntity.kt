package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistEntity (
    @PrimaryKey(autoGenerate = true) @ColumnInfo("playlist_id") val playlistId: Int = 0,
    @ColumnInfo("avatar_path") val avatarPath: String,
    @ColumnInfo("playlist_name") val playlistName: String,
    val description: String,
    @ColumnInfo("created_at") val createdAt: Long = System.currentTimeMillis()
)