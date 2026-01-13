package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Presentation.model.search.TrackAdapter
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.main.activity.MainActivity
import com.example.playlistmaker.ui.media.view_model.FavoritesViewModel
import com.example.playlistmaker.ui.player.fragment.MediaFragment
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {

    private var binding: FavoritesFragmentBinding? = null
    private val viewModel: FavoritesViewModel by viewModel()

    private var adapter: TrackAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).checkBottomNavigationView()

        adapter = TrackAdapter(emptyList(), viewModel::onTrackClick) { (activity as MainActivity).animateBottomNavigationView() }
        binding?.trackRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.trackRecyclerView?.adapter = adapter

        viewModel.getFavoritesState().observe(viewLifecycleOwner){ state ->
            when(state.isNotEmpty()){
                true -> {
                    binding?.trackRecyclerView?.isVisible = true
                    adapter?.updateData(state)
                    binding?.resNotEx?.visibility = View.GONE
                    binding?.errorText?.visibility = View.GONE
                }
                false -> {
                    binding?.trackRecyclerView?.visibility = View.GONE
                    binding?.resNotEx?.isVisible = true
                    binding?.errorText?.isVisible = true
                }
            }
        }

        viewModel.getTrackClickEvent().observe(viewLifecycleOwner) { item ->
            findNavController().navigate(
                R.id.action_mediaPlayerFragment2_to_mediaFragment2,
                bundleOf(MediaFragment.ARGS_TRACK to item)
            )
        }

        viewModel.render()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        fun newInstance() = FavoritesFragment()
    }
}