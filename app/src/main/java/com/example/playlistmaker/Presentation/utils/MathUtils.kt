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

fun Long.toMinutes(): String {
    val duration = Duration.ofMillis(this)
    val minutes = duration.toMinutes()

    val remainder10 = minutes % 10
    val remainder100 = minutes % 100

    val word = when {
        remainder100 in 11..14 -> "минут"
        remainder10 == 1L -> "минута"
        remainder10 in 2..4 -> "минуты"
        else -> "минут"
    }

    return "$minutes $word"
}
