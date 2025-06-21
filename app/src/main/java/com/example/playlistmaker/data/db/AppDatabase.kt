package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoritesDao
import com.example.playlistmaker.data.db.dao.PlaylistTrackDao
import com.example.playlistmaker.data.db.dao.PlaylistsDao
import com.example.playlistmaker.data.db.entity.FavoritesEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.data.db.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, FavoritesEntity::class, PlaylistTrackCrossRef::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    abstract fun playlistsDao(): PlaylistsDao

    abstract fun playlistTrackDao(): PlaylistTrackDao
}