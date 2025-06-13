package com.example.playlistmaker.UI.player.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Domain.Track
import com.example.playlistmaker.Presentation.state.PlayerState
import com.example.playlistmaker.Presentation.utils.convertMillisToMinutesAndSeconds
import com.example.playlistmaker.Presentation.utils.dateFormatter
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.view_model.MediaViewModel
import com.example.playlistmaker.databinding.MediaFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment: Fragment() {

    private lateinit var binding: MediaFragmentBinding

    private val viewModel by viewModel<MediaViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MediaFragmentBinding.inflate(inflater, container, false)
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = savedInstanceState?.getParcelable<Track>(ARGS_TRACK) ?: requireArguments().getParcelable(ARGS_TRACK)

        binding.back.setOnClickListener {
            if (isAdded) {
                parentFragmentManager.popBackStack()
            }
        }

        binding.play.setOnClickListener {
            viewModel.playbackControl()
        }

        if (track != null) {
            binding.artistName.text = track.artistName
            binding.albumName.text = track.collectionName
            binding.albumValue.text = binding.albumName.text
            binding.genreValue.text = track.primaryGenreName
            binding.countryValue.text = track.country

            binding.durationValue.text = convertMillisToMinutesAndSeconds(track.trackTimeMillis)
            binding.yearValue.text = dateFormatter(track.releaseDate).year.toString()

            val bigImage = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
            Glide.with(this)
                .load(bigImage)
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.bigplaceholder)
                .into(binding.albumImage)

            viewModel.getLikeState().observe(viewLifecycleOwner) {state ->
                when(state) {
                    true -> binding.like.setImageResource(R.drawable.like_enabled)
                    false -> binding.like.setImageResource(R.drawable.like_disabled)
                }
            }

            binding.like.setOnClickListener {
                viewModel.like(track)
            }

            viewModel.preparePlayer(track)
        } else {
            Log.e("MediaActivity", "Track data is null")
        }

        viewModel.playerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlayerState.Playing -> {
                    binding.play.setImageResource(R.drawable.pause)
                    binding.time.text = state.progress
                }
                is PlayerState.Paused -> {
                    binding.play.setImageResource(R.drawable.play)
                    binding.time.text = state.progress
                }
                is PlayerState.Prepared -> {
                    binding.play.isEnabled = true
                    binding.play.setImageResource(R.drawable.play)
                    binding.time.text = state.progress
                }
                is PlayerState.Default -> {}
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.savePlayerState()
        viewModel.pausePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val track = requireArguments().getParcelable<Track>("track")
        if (track != null) {
            outState.putParcelable("track", track)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving) {
            viewModel.releasePlayer()
        }
    }

    companion object {

        const val ARGS_TRACK = "track_id"
    }
}