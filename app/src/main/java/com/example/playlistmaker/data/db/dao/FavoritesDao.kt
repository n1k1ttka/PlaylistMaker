package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(track: TrackEntity): Long

    @Query("SELECT * FROM favorites_table")
    suspend fun getFavorites(): List<TrackEntity>

    @Delete
    suspend fun deleteFromFavorites(track: TrackEntity)
}