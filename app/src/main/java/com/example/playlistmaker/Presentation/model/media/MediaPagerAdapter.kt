package com.example.playlistmaker.Presentation.model.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.fragment.FavoritesFragment
import com.example.playlistmaker.ui.media.fragment.PlayListsFragment

class MediaPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesFragment.newInstance() else PlayListsFragment.newInstance()
    }
}