package com.example.playlistmaker.Data.network

import com.example.playlistmaker.Data.dto.ITunesResponce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ITunesApiService {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponce>
}