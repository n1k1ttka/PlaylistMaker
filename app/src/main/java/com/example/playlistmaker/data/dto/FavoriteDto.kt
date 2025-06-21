package com.example.playlistmaker.data.dto

data class FavoriteDto(
    val previewUrl: String,
    val trackId: Int,
    val trackName: String, // Название композиции
    val collectionName: String, // Альбом
    val artistName: String, // Имя исполнителя
    val country: String, // Страна
    val primaryGenreName: String, // Жанр
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val releaseDate: String, // Дата релиза
)
