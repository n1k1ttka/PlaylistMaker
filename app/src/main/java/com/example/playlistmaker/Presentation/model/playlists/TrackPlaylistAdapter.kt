package com.example.playlistmaker.Presentation.model.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SongItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TrackPlaylistAdapter(
    private var tracks: List<ParcelableTrack>,
    private val onItemClick: (ParcelableTrack) -> Unit,
    private val onLongItemClick: (ParcelableTrack) -> Unit
): RecyclerView.Adapter<TrackPlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackPlaylistViewHolder {
        val binding = SongItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TrackPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackPlaylistViewHolder, position: Int) {
        val track = tracks[position]
        holder.binding.trackName.text = track.trackName
        holder.binding.artistName.text = track.artistName
        holder.binding.artistName.requestLayout()
        holder.binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        Glide.with(holder.itemView)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(2.dpToPx(holder.itemView.context)))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.artworkUrl100)

        holder.binding.root.setOnClickListener {
            onItemClick(track)
        }

        holder.binding.root.setOnLongClickListener {
            onLongItemClick(track)
            true
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun updateData(newTracks: List<ParcelableTrack>) {
        tracks = newTracks
        notifyDataSetChanged()
    }
}