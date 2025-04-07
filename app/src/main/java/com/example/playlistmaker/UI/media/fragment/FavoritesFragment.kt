package com.example.playlistmaker.UI.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.UI.media.view_model.FavoritesViewModel
import com.example.playlistmaker.databinding.FavoritesFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {

    private lateinit var binding: FavoritesFragmentBinding
    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavoritesFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.something = requireArguments().getInt(NUMBER).toString()
    }

    companion object {

        fun newInstance() = FavoritesFragment()
    }
}