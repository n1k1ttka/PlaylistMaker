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
import com.example.playlistmaker.databinding.MiniPlaylistItemBinding
import java.io.File

class MiniPlaylistAdapter(
    private var playlists: List<Playlist>,
    private val onItemClick: (Playlist) -> Unit
): RecyclerView.Adapter<MiniPlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniPlaylistViewHolder {
        val binding = MiniPlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MiniPlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MiniPlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        holder.binding.playlistTitle.text = playlist.playlistName
        holder.binding.trackCount.text = getTracksCountString(playlist.tracksCount)
        Glide.with(holder.itemView)
            .load(File(playlist.avatarPath))
            .transform(RoundedCorners(2.dpToPx(holder.itemView.context)))
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.binding.playlistImage)

        holder.itemView.setOnClickListener {
            onItemClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}