package com.example.playlistmaker.utils

import android.content.Context
import android.content.res.Configuration


fun isNightMode(context: Context): Boolean {
    val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return when (currentNightMode) {
        Configuration.UI_MODE_NIGHT_YES -> true // Ночная тема
        else -> false // Дневная тема
    }
}