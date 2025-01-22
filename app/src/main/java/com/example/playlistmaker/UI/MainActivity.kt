package com.example.playlistmaker.UI

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Domain.Creator
import com.example.playlistmaker.Domain.ThemeInteractor
import com.example.playlistmaker.Domain.TrackInteractor
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.isNightMode

class MainActivity : AppCompatActivity() {

    private lateinit var interactor: ThemeInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        interactor = Creator.provideThemeInteractor(this)

        if (interactor.loadTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            interactor.saveTheme(true)
        } else {
            if (isNightMode(this)){
                AppCompatDelegate.MODE_NIGHT_YES
                interactor.saveTheme(true)
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
                interactor.saveTheme(false)
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