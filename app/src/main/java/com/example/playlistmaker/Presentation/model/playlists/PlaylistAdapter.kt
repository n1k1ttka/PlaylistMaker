package com.example.playlistmaker.Presentation.model.playlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.Presentation.utils.getTracksCountString
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import java.io.File

class PlaylistAdapter(
    private var playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit,
    private val animation: () -> Unit
): RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        holder.binding.playlistTitle.text = playlist.playlistName
        holder.binding.trackCount.text = getTracksCountString(playlist.tracksCount)
        Glide.with(holder.itemView)
            .load(File(playlist.avatarPath))
            .transform(RoundedCorners(8.dpToPx(holder.itemView.context)))
            .placeholder(R.drawable.bigplaceholder)
            .into(holder.binding.playlistImage)

        holder.itemView.setOnClickListener {
            //animation()
            onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}