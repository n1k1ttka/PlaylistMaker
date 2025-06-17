package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteTrack(track: FavoritesEntity): Long

    @Delete
    suspend fun deleteFavoriteTrack(track: FavoritesEntity)

    @Query("SELECT * FROM favorite_tracks_table ORDER BY added_at DESC")
    fun getAllFavoriteTracks(): Flow<List<FavoritesEntity>>
}