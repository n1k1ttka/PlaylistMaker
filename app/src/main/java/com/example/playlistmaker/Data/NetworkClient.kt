package com.example.playlistmaker.Data

import com.example.playlistmaker.Data.dto.Responce

interface NetworkClient {
    fun load(dto: Any): Responce
}