package com.example.playlistmaker.Presentation.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun dateFormatter(dateString: String): LocalDateTime {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val localDateTime = LocalDateTime.parse(dateString, formatter)
    return localDateTime
}