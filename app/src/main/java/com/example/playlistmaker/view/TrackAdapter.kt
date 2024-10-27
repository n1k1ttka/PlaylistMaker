package com.example.playlistmaker.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.lang.Exception

const val STORYSIZE = 10

class TrackAdapter(
    private val tracks: List<Track>,
    private val story: MutableList<Track>
): RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false))
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            var inStory = false
            if (story.contains(tracks[position])) {
                story.remove(tracks[position])
                story.add(0, tracks[position])
                inStory = true
            }
            if (!inStory) {
                if (story.size < STORYSIZE) {
                    story.add(0, tracks[position])
                } else {
                    story.removeAt(9)
                    story.add(0, tracks[position])
                }
            }
        }
    }

}