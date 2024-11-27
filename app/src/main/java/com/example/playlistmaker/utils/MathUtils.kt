package com.example.playlistmaker.utils

import java.time.Duration

fun convertMillisToMinutesAndSeconds(millis: Long): String {
    val duration = Duration.ofMillis(millis)

    val minutes = duration.toMinutes()
    val seconds = duration.minusMinutes(minutes).seconds

    return "%02d:%02d".format(minutes, seconds)
}
