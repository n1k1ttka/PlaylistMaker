package com.example.playlistmaker.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.model.Track
import java.lang.Exception

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
                //searchHistory.saveHistory(story)
            }
            if (!inStory) {
                if (story.size < 10) {
                    story.add(0, tracks[position])
                    //searchHistory.saveHistory(story)
                } else {
                    story.removeAt(9)
                    story.add(0, tracks[position])
                    //searchHistory.saveHistory(story)
                }
            }
        }
    }

}