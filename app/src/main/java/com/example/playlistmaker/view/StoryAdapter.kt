package com.example.playlistmaker.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track

class StoryAdapter(
    private val story: MutableList<Track>
):  RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false))
    }

    override fun getItemCount(): Int = story.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(story[position])
    }
}