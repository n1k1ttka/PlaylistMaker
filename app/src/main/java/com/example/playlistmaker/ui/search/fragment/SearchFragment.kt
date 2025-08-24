package com.example.playlistmaker.ui.search.fragment

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.Presentation.InternetConnectionBroadcastReceiver
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.player.fragment.MediaFragment
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.search.layout.SearchScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val receiver = InternetConnectionBroadcastReceiver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(
                    viewModel = viewModel,
                    onTrackClick = { track ->
                        viewModel.onTrackClick(track)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_mediaFragment,
                            bundleOf(MediaFragment.ARGS_TRACK to track)
                        )
                    },
                    onStoryClick = { track ->
                        viewModel.triggerEvent(track)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_mediaFragment,
                            bundleOf(MediaFragment.ARGS_TRACK to track)
                        )
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        requireContext().registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(receiver)
    }
}