package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class MediaPlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    // Передаём сам фрагмент, чтобы взять childFragmentManager и lifecycle
                    MediaPlayerScreen(host = this@MediaPlayerFragment)
                }
            }
        }
    }

    companion object {
        fun newInstance() = MediaPlayerFragment()
    }
}

