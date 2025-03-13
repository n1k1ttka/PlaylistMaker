package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Инициализация Creator с ApplicationContext
        Creator.init(this)
    }
}