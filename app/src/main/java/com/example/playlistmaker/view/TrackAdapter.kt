package com.example.playlistmaker.view

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.MediaActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.lang.Exception

const val STORYSIZE = 10

class TrackAdapter(
    private val tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
): RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            // дебаунс делать тут или в лямбде?
            onItemClick(tracks[position])
        }
    }

}