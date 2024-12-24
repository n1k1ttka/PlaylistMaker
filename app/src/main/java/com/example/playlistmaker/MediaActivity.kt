package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
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

    private var mediaPlayer = MediaPlayer()
    private var mainThreadHandler: Handler? = null
    private var updateTimerTask: Runnable? = null
    private var secondsLeftTextView: TextView? = null
    private var mediaUrl: String = ""
    private lateinit var playButton: ImageButton
    private var secondsCount = 30000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainThreadHandler = Handler(Looper.getMainLooper())
        secondsLeftTextView = findViewById(R.id.time)

        val track = intent.getParcelableExtra<Track>("track")

        val backBttn = findViewById<ImageView>(R.id.back)
        playButton = findViewById(R.id.play)
        val backClickListener: View.OnClickListener = View.OnClickListener { finish() }
        backBttn.setOnClickListener(backClickListener)
        playButton.setOnClickListener {
            playbackControl()
        }

        if (track != null){
            val artist = findViewById<TextView>(R.id.artist_name)
            val albumName = findViewById<TextView>(R.id.album_name)
            val duration = findViewById<TextView>(R.id.duration_value)
            val album = findViewById<TextView>(R.id.album_value)
            val year = findViewById<TextView>(R.id.year_value)
            val genre = findViewById<TextView>(R.id.genre_value)
            val country = findViewById<TextView>(R.id.country_value)
            val albumImage = findViewById<ImageView>(R.id.album_image)

            mediaUrl = track.previewUrl
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

        preparePlayer(playButton)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun preparePlayer(play: ImageButton) {
        mediaPlayer.setDataSource(mediaUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            play.setImageResource(R.drawable.play)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause)
        timer("play")
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play)
        timer("pause")
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun timer(command: String) {
        if (command.equals("play")) {
            if (updateTimerTask == null) {
                updateTimerTask = createUpdateTimerTask()
                mainThreadHandler?.post(updateTimerTask!!)
            }
        } else if (command.equals("pause")) {
            updateTimerTask?.let {
                mainThreadHandler?.removeCallbacks(it)
            }
            updateTimerTask = null // Очищаем ссылку на задачу
        }
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (secondsCount > 0) {
                    val seconds = secondsCount / DELAY
                    secondsLeftTextView?.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                    secondsCount -= DELAY
                    mainThreadHandler?.postDelayed(this, DELAY)
                } else {
                    // Если таймер окончен, выводим текст
                    secondsLeftTextView?.text = "0:00"
                    secondsCount = 30000L
                }
            }
        }
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 1000L
    }

    private var playerState = STATE_DEFAULT
}