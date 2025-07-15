package com.example.playlistmaker.UI.player.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Presentation.model.ParcelableTrack
import com.example.playlistmaker.Presentation.model.playlists.MiniPlaylistAdapter
import com.example.playlistmaker.Presentation.state.PlayerState
import com.example.playlistmaker.Presentation.state.PlaylistState
import com.example.playlistmaker.Presentation.utils.convertMillisToMinutesAndSeconds
import com.example.playlistmaker.Presentation.utils.dateFormatter
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.main.activity.MainActivity
import com.example.playlistmaker.UI.player.view_model.MediaViewModel
import com.example.playlistmaker.databinding.MediaFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaFragment: Fragment() {

    private lateinit var binding: MediaFragmentBinding
    private var adapter: MiniPlaylistAdapter? = null

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

        val track = savedInstanceState?.getParcelable<ParcelableTrack>(ARGS_TRACK) ?: requireArguments().getParcelable(ARGS_TRACK)

        val bottomSheetContainer = binding.standardBottomSheet
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 24f)
            .setTopRightCorner(CornerFamily.ROUNDED, 24f)
            .build()

        val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ColorStateList.valueOf(resources.getColor(R.color.white_black))
            elevation = bottomSheetContainer.elevation
        }

        bottomSheetContainer.background = materialShapeDrawable
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.back.setOnClickListener {
            if (isAdded) {
                findNavController().popBackStack()
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
                .transform(RoundedCorners(2.dpToPx(requireContext())))
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
//                    binding.play.setImageResource(R.drawable.pause)
                    binding.play.setPlaying(true)
                    binding.time.text = state.progress
                }
                is PlayerState.Paused -> {
//                    binding.play.setImageResource(R.drawable.play)
                    binding.play.setPlaying(false)
                    binding.time.text = state.progress
                }
                is PlayerState.Prepared -> {
                    binding.play.isEnabled = true
//                    binding.play.setImageResource(R.drawable.play)
                    binding.play.setPlaying(false)
                    binding.time.text = state.progress
                }
                is PlayerState.Default -> {}
            }
        }

        binding.playlists.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.getPlaylists()
        }

        adapter = MiniPlaylistAdapter(emptyList(), viewModel::onPlaylistClick)
        binding.playlistsList.layoutManager = LinearLayoutManager(context)
        binding.playlistsList.adapter = adapter

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            when(state){
                is PlaylistState.WebPlaylists -> {
                    when(state.playlists.isNotEmpty()){
                        true -> {
                            adapter?.updateData(state.playlists)
                            binding.playlistsList.isVisible = true
                            binding.resNotEx.visibility = View.GONE
                            binding.errorText.visibility = View.GONE
                        }
                        false -> {
                            binding.playlistsList.visibility = View.GONE
                            binding.resNotEx.isVisible = true
                            binding.errorText.isVisible = true
                        }
                    }
                }
                is PlaylistState.PlaylistClick -> {
                    when(state.addedInPlaylist) {
                        true -> {
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                            binding.playlistsList.adapter?.notifyDataSetChanged()
                            Snackbar.make(
                                requireView(),
                                getString(R.string.added_to_playlist, state.selectedPlaylist?.playlistName),
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.black_white))
                                .setTextColor(resources.getColor(R.color.white_black))
                                .show()
                        }
                        false -> {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.not_added_to_playlist, state.selectedPlaylist?.playlistName),
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.black_white))
                                .setTextColor(resources.getColor(R.color.white_black))
                                .show()
                        }
                    }
                }
            }
        }

        binding.newPlaylist.setOnClickListener {
            (activity as MainActivity).animateBottomNavigationView()
            findNavController().navigate(
                R.id.action_mediaFragment_to_newPlayListFragment
            )
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.savePlayerState()
        viewModel.pausePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val track = requireArguments().getParcelable<ParcelableTrack>("track")
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