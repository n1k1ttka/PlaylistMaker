package com.example.playlistmaker.Presentation.utils

import android.content.Context
import java.time.Duration

fun convertMillisToMinutesAndSeconds(millis: Long): String {
    val duration = Duration.ofMillis(millis)

    val minutes = duration.toMinutes()
    val seconds = duration.minusMinutes(minutes).seconds

    return "%02d:%02d".format(minutes, seconds)
}

fun Int.dpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.density
    return (this * density).toInt()
}
