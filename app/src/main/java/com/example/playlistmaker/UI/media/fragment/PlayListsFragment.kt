package com.example.playlistmaker.UI.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.Presentation.model.playlists.PlaylistAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.UI.main.activity.MainActivity
import com.example.playlistmaker.UI.media.view_model.PlayListsViewModel
import com.example.playlistmaker.UI.playlist.fragment.PlaylistFragment
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment: Fragment() {

    private var binding: PlaylistsFragmentBinding? = null
    private val viewModel: PlayListsViewModel by viewModel()

    private var adapter: PlaylistAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistsFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).checkBottomNavigationView()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).checkBottomNavigationView()

        adapter = PlaylistAdapter(emptyList(), viewModel::onPlaylistClick) { (activity as MainActivity).animateBottomNavigationView() }
        binding?.playlistRecyclerView?.adapter = adapter

        viewModel.getPlaylistState().observe(viewLifecycleOwner) { state ->
            when(state.isNotEmpty()){
                true -> {
                    adapter?.updateData(state)
                    binding?.playlistRecyclerView?.isVisible = true
                    binding?.resNotEx?.visibility = View.GONE
                    binding?.errorText?.visibility = View.GONE
                }
                false -> {
                    binding?.playlistRecyclerView?.visibility = View.GONE
                    binding?.resNotEx?.isVisible = true
                    binding?.errorText?.isVisible = true
                }
            }
        }

        viewModel.getPlaylistClickEvent().observe(viewLifecycleOwner) { playlist ->
            (activity as MainActivity).animateBottomNavigationView()
            findNavController().navigate(
                R.id.action_mediaPlayerFragment2_to_playlistFragment,
                bundleOf(PlaylistFragment.ARGS_PLAYLIST to playlist.id)
            )
        }

        binding?.newPlaylist?.setOnClickListener {
            (activity as MainActivity).animateBottomNavigationView()
            findNavController().navigate(
                R.id.action_mediaPlayerFragment2_to_newPlayListFragment
            )
        }

        viewModel.render()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        fun newInstance() = PlayListsFragment()
    }
}