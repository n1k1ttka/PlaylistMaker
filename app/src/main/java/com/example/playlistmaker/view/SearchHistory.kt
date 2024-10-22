package com.example.playlistmaker.view

import android.content.SharedPreferences
import com.example.playlistmaker.model.Track
import com.google.gson.Gson

const val TRACK_HISTORY_KEY = "key_for_track_history"

class SearchHistory(
    val sharedPrefs: SharedPreferences
) {
    fun readHistory(): MutableList<Track> {
        val json = sharedPrefs.getString(TRACK_HISTORY_KEY, null)
        return Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    fun saveHistory(history: List<Track>){
        val json = Gson().toJson(history)
        sharedPrefs.edit()
            .putString(TRACK_HISTORY_KEY, json)
            .apply()
    }
}