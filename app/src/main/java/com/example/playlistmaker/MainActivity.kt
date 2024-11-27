package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.utils.isNightMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPrefs = getSharedPreferences(NIGHT_MODE_PREFERENCES, MODE_PRIVATE)
        if (sharedPrefs.getBoolean(DAYNIGHT_SWITCHER_KEY, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefs.edit()
                .putBoolean(DAYNIGHT_SWITCHER_KEY, true)
                .apply()
        } else {
            if (isNightMode(this)){
                AppCompatDelegate.MODE_NIGHT_YES
                sharedPrefs.edit()
                    .putBoolean(DAYNIGHT_SWITCHER_KEY, true)
                    .apply()
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
                sharedPrefs.edit()
                    .putBoolean(DAYNIGHT_SWITCHER_KEY, false)
                    .apply()
            }
        }

        val searchButton = findViewById<Button>(R.id.search)
        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val mediaButton = findViewById<Button>(R.id.media)
        val mediaClickListener: View.OnClickListener = View.OnClickListener { startActivity(Intent(this, MediaPlayerActivity::class.java)) }
        mediaButton.setOnClickListener(mediaClickListener)

        val settingButton = findViewById<Button>(R.id.settings)
        val settingClickListener: View.OnClickListener = View.OnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
        settingButton.setOnClickListener(settingClickListener)
    }
}