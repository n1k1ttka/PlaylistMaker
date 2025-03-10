package com.example.playlistmaker.UI.media.activity

import com.example.playlistmaker.UI.media.view_model.MediaViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.R
import com.example.playlistmaker.Presentation.utils.convertMillisToMinutesAndSeconds
import com.example.playlistmaker.Presentation.utils.dateFormatter
import com.example.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[MediaViewModel::class]
    }

    private var mainThreadHandler: Handler? = null
    private var mediaUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>("track")

        binding.back.setOnClickListener { finish() }

        binding.play.setOnClickListener {
            viewModel.playbackControl()
        }

        if (track != null) {
            mediaUrl = track.previewUrl
            binding.artistName.text = track.artistName
            binding.albumName.text = track.collectionName
            binding.albumValue.text = binding.albumName.text
            binding.genreValue.text = track.primaryGenreName
            binding.countryValue.text = track.country

            binding.durationValue.text = convertMillisToMinutesAndSeconds(track.trackTimeMillis)
            binding.yearValue.text = dateFormatter(track.releaseDate).year.toString()

            val bigImage = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            Glide.with(this)
                .load(bigImage)
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.bigplaceholder)
                .into(binding.albumImage)

            viewModel.preparePlayer(track.previewUrl)
        } else {
            Log.e("MediaActivity", "Track data is null")
        }

        viewModel.playerState.observe(this) { state ->
            when (state) {
                is MediaViewModel.PlayerState.Playing -> {
                    binding.play.setImageResource(R.drawable.pause)
                    binding.time.text = state.timeLeft
                }
                MediaViewModel.PlayerState.Paused -> {
                    binding.play.setImageResource(R.drawable.play)
                }
                MediaViewModel.PlayerState.Prepared -> {
                    binding.play.isEnabled = true
                }
                else -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        fun show(context: Context, track: Track) {
            val intent = Intent(context, MediaActivity::class.java)
            intent.putExtra("track", track)
            context.startActivity(intent)
        }
    }
}