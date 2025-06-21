package com.example.playlistmaker.UI.playlist.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Presentation.model.playlists.TrackPlaylistAdapter
import com.example.playlistmaker.Presentation.state.PlaylistTrackState
import com.example.playlistmaker.Presentation.utils.dpToPx
import com.example.playlistmaker.Presentation.utils.getTracksCountString
import com.example.playlistmaker.Presentation.utils.toMinutes
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.player.fragment.MediaFragment
import com.example.playlistmaker.UI.playlist.view_model.PlaylistViewModel
import com.example.playlistmaker.databinding.PlaylistFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment(): Fragment() {

    private lateinit var binding: PlaylistFragmentBinding

    private val viewModel by viewModel<PlaylistViewModel>()
    private var adapter: TrackPlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PlaylistFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = (savedInstanceState?.getSerializable(ARGS_PLAYLIST) ?: requireArguments().getSerializable(ARGS_PLAYLIST)) as? Int

        val bottomSheetContainer = binding.standardBottomSheet
        val shapeAppearanceModel = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, resources.getDimension(R.dimen.radius_16))
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimension(R.dimen.radius_16))
            .build()

        val materialShapeDrawable = MaterialShapeDrawable(shapeAppearanceModel).apply {
            fillColor = ContextCompat.getColorStateList(requireContext(), R.color.white_black)
            elevation = bottomSheetContainer.elevation
        }

        bottomSheetContainer.background = materialShapeDrawable
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        binding.back.setOnClickListener {
            if (isAdded) {
                findNavController().popBackStack()
            }
        }

        binding.tracksList.layoutManager = LinearLayoutManager(requireContext())
        adapter = TrackPlaylistAdapter(emptyList(), viewModel::onTrackClick, viewModel::onLongTrackClick)
        binding.tracksList.adapter = adapter

        playlistId?.let { viewModel.render(playlistId) }
        viewModel.getPlaylistState().observe(viewLifecycleOwner) { playlistState ->
            binding.playlistName.text = playlistState.playlist.playlistName
            binding.description.text = playlistState.playlist.description
            binding.duration.text = playlistState.playlist.tracksDuration.toMinutes()
            binding.trackCount.text = getTracksCountString(playlistState.playlist.tracksCount)
            Glide.with(this)
                .load(playlistState.playlist.avatarPath)
                .transform(RoundedCorners(2.dpToPx(requireContext())))
                .placeholder(R.drawable.playlist_placeholder)
                .into(binding.playlistImage)

            renderMiniPlaylist(playlistState)

            adapter?.updateData(playlistState.tracks)
            when(playlistState.tracks.isNotEmpty()) {
                true -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                false -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner){ item ->
            findNavController().navigate(
                R.id.action_playlistFragment_to_mediaFragment,
                bundleOf(MediaFragment.ARGS_TRACK to item)
            )
        }

        viewModel.getLongTrackClickEvent().observe(viewLifecycleOwner){ item ->
            val dialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.want_to_delete)
                .setPositiveButton(R.string.yes) { _, _ ->
                    playlistId?.let {
                        viewModel.deleteTrack(item, viewModel.getPlaylistState().value!!.playlist)
                        viewModel.render(playlistId)
                    }
                }
                .setNegativeButton(R.string.no, null)
                .create()

            dialog.setOnShowListener {
                val positiveColor = ContextCompat.getColor(requireContext(), R.color.blue)
                val negativeColor = ContextCompat.getColor(requireContext(), R.color.blue)

                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(positiveColor)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(negativeColor)
            }

            dialog.show()
        }

        binding.shareButton.setOnClickListener {
            sharing()
        }

        binding.shareTextView.setOnClickListener {
            sharing()
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            val dialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.want_to_delete_playlist)
                .setPositiveButton(R.string.yes) { _, _ ->
                    playlistId?.let {
                        viewModel.deletePlaylist()
                        findNavController().popBackStack()
                    }
                }
                .setNegativeButton(R.string.no, null)
                .create()

            dialog.setOnShowListener {
                val positiveColor = ContextCompat.getColor(requireContext(), R.color.blue)
                val negativeColor = ContextCompat.getColor(requireContext(), R.color.blue)

                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(positiveColor)
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(negativeColor)
            }

            dialog.show()
        }

        binding.redact.setOnClickListener {
            viewModel.getPlaylistState().value.let {
                redact(viewModel.getPlaylistState().value!!.playlist)
            }
        }

        binding.triplePoints.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.tracksList.isVisible = false
            binding.dropdownMenu.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        if (!viewModel.getPlaylistState().value?.tracks.isNullOrEmpty()) {
                            binding.tracksList.isVisible = true
                            binding.dropdownMenu.isVisible = false
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun renderMiniPlaylist(playlistState: PlaylistTrackState){
        binding.playlistTitle.text = playlistState.playlist.playlistName
        binding.miniTrackCount.text = getTracksCountString(playlistState.playlist.tracksCount)
        Glide.with(this)
            .load(playlistState.playlist.avatarPath)
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.miniPlaylistImage)
    }

    private fun sharing(){
        when(viewModel.getPlaylistState().value?.tracks.isNullOrEmpty()){
            true -> Toast.makeText(requireContext(), "В этом плейлисте нет списка треков, которыми можно поделиться" ,Toast.LENGTH_LONG).show()
            false -> {
                val message = viewModel.generatePlaylistMessage(
                    viewModel.getPlaylistState().value?.playlist,
                    viewModel.getPlaylistState().value?.tracks
                )
                sharePlaylist(message)
            }
        }
    }

    private fun redact(item: Playlist){
        findNavController().navigate(
            R.id.action_playlistFragment_to_newPlayListFragment2,
            bundleOf( ARGS_PLAYLIST to item)
        )
    }

    private fun sharePlaylist(playlistText: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, playlistText)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Поделиться плейлистом через...")
        startActivity(shareIntent)
    }

    companion object {

        const val ARGS_PLAYLIST = "playlist_id"
    }
}