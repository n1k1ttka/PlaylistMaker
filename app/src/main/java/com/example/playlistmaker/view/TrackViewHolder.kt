package com.example.playlistmaker.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val item: View): RecyclerView.ViewHolder(item) {

    private val trackName: TextView = item.findViewById(R.id.track_name) // Название композиции
    private val artistName: TextView = item.findViewById(R.id.artist_name) // Имя исполнителя
    private val trackTime: TextView = item.findViewById(R.id.track_time)// Продолжительность трека
    private val artworkUrl100: ImageView = item.findViewById(R.id.artwork_url_100)// Ссылка на изображение обложки

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        artistName.requestLayout()
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis.toInt())
        Glide.with(item.context)
            .load(model.artworkUrl100)
            //.centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.ic_placeholder)
            .into(artworkUrl100)
    }
}