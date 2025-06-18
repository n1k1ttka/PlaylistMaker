package com.example.playlistmaker.Presentation.model.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.model.ParcelableTrack

class StoryAdapter(
    private var story: List<ParcelableTrack>,
    private val onItemClick: (ParcelableTrack) -> Unit,
    private val animation: () -> Unit
):  RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false))
    }

    override fun getItemCount(): Int = story.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(story[position])
        holder.itemView.setOnClickListener {
            animation()
            onItemClick(story[position])
        }
    }

    fun updateData(newTracks: List<ParcelableTrack>) {
        story = newTracks
        notifyDataSetChanged()
    }
}