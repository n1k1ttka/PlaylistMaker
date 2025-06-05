package com.example.playlistmaker.Domain.search

import com.example.playlistmaker.Domain.Resource
import com.example.playlistmaker.Domain.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun getTracks(text: String): Flow<Resource<List<Track>>>
}