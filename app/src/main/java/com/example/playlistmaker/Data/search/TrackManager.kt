package com.example.playlistmaker.Data.search

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.Data.dto.TrackDto
import com.google.gson.Gson

const val TRACK_HISTORY_KEY = "key_for_track_history"

class TrackManager(val sharedPrefs: SharedPreferences) {

    fun readHistory(): List<TrackDto> {
        val json = sharedPrefs.getString(TRACK_HISTORY_KEY, null) ?: return listOf()
        return Gson().fromJson(json, Array<TrackDto>::class.java).toList()
    }

    fun saveHistory(history: List<TrackDto>){
        val json = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(TRACK_HISTORY_KEY, json)
            .apply()
    }
}