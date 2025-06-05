package com.example.playlistmaker.data.search

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.Gson

const val TRACK_HISTORY_KEY = "key_for_track_history"

class TrackManager(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) {

    fun readHistory(): List<TrackDto> {
        return try {
            val json = sharedPrefs.getString(TRACK_HISTORY_KEY, null) ?: return listOf()
            gson.fromJson(json, Array<TrackDto>::class.java).toList()
        } catch (e: Exception) {
            Log.e("TrackManager", "Error reading history", e)
            listOf()
        }
    }

    fun saveHistory(history: List<TrackDto>){
        val json = gson.toJson(history)
        sharedPrefs.edit()
            .putString(TRACK_HISTORY_KEY, json)
            .apply()
    }
}