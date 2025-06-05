package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class TrackEntity(
    val previewUrl: String,
    @PrimaryKey val trackId: Int,
    val trackName: String, // Название композиции
    val collectionName: String, // Альбом
    val artistName: String, // Имя исполнителя
    val country: String, // Страна
    val primaryGenreName: String, // Жанр
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val releaseDate: String, // Дата релиза
)