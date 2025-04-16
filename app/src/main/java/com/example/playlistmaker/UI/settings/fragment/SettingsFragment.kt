package com.example.playlistmaker.UI.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.playlistmaker.Presentation.model.search.StoryAdapter
import com.example.playlistmaker.Presentation.model.search.TrackAdapter
import com.example.playlistmaker.UI.settings.view_model.SettingsViewModel
import com.example.playlistmaker.databinding.SettingsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    private lateinit var binding: SettingsFragmentBinding

    private val viewModel by viewModel<SettingsViewModel>()

    private lateinit var adapter: TrackAdapter
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shareButton.setOnClickListener {
            startActivity(viewModel.getShareIntent())
        }

        binding.support.setOnClickListener {
            startActivity(viewModel.getSupportIntent())
        }

        binding.contract.setOnClickListener {
            startActivity(viewModel.getContractIntent())
        }

        viewModel.themeState.observe(viewLifecycleOwner) { isDarkTheme ->
            binding.modeSwitch.isChecked = isDarkTheme
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

        binding.modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }
    }
}