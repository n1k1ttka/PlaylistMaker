package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Responce

interface NetworkClient {
    suspend fun load(dto: Any): Responce
}