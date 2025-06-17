package com.example.playlistmaker.Presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun dateFormatter(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime
}

fun getTracksCountString(count: Int): String {
    val remainder10 = count % 10
    val remainder100 = count % 100

    val word = when {
        remainder100 in 11..14 -> "треков"
        remainder10 == 1 -> "трек"
        remainder10 in 2..4 -> "трека"
        else -> "треков"
    }

    return "$count $word"
}