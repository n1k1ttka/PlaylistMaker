package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylist(playlist: PlaylistEntity): Long

    @Query("SELECT * FROM playlists_table ORDER BY created_at DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists_table WHERE playlist_id = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistEntity

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)
}