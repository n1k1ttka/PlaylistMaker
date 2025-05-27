package com.example.playlistmaker.Data.network

import com.example.playlistmaker.Data.dto.Responce

interface NetworkClient {
    suspend fun load(dto: Any): Responce
}