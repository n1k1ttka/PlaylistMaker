package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.utils.convertMillisToMinutesAndSeconds
import com.example.playlistmaker.utils.dateFormatter

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backBttn = findViewById<ImageView>(R.id.back)
        val backClickListener: View.OnClickListener = View.OnClickListener { finish() }
        backBttn.setOnClickListener(backClickListener)

        val track = intent.getParcelableExtra<Track>("track")

        if (track != null){
            val artist = findViewById<TextView>(R.id.artist_name)
            val albumName = findViewById<TextView>(R.id.album_name)
            val duration = findViewById<TextView>(R.id.duration_value)
            val album = findViewById<TextView>(R.id.album_value)
            val year = findViewById<TextView>(R.id.year_value)
            val genre = findViewById<TextView>(R.id.genre_value)
            val country = findViewById<TextView>(R.id.country_value)
            val albumImage = findViewById<ImageView>(R.id.album_image)

            artist.text = track.artistName
            albumName.text = track.collectionName
            album.text = albumName.text
            genre.text = track.primaryGenreName
            country.text = track.country

            duration.text = convertMillisToMinutesAndSeconds(track.trackTimeMillis)
            year.text = dateFormatter(track.releaseDate).year.toString()

            val bigImage = track.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
            Glide.with(this)
                .load(bigImage)
                //.centerCrop()
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.bigplaceholder)
                .into(albumImage)
        }
    }
}