package com.example.playlistmaker.UI.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.UI.media.view_model.PlayListsViewModel
import com.example.playlistmaker.databinding.PlaylistsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListsFragment: Fragment() {

    private var binding: PlaylistsFragmentBinding? = null
    private val playListsViewModel: PlayListsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PlaylistsFragmentBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {

        fun newInstance() = PlayListsFragment()
    }
}