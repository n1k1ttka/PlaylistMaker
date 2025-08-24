package com.example.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import com.example.playlistmaker.ui.settings.layout.SettingsScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Используем ComposeView вместо XML
        return ComposeView(requireContext()).apply {
            setContent {
                // Можно подключить MaterialTheme
                MaterialTheme {
                    SettingsScreen(viewModel = viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themeState.observe(viewLifecycleOwner) { isDarkTheme ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }
}
