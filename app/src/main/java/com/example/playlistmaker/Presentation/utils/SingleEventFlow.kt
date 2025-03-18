package com.example.playlistmaker.Presentation.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SingleEventFlow<T> {

    private val _events = MutableSharedFlow<T>(replay = 0)

    val events: SharedFlow<T> = _events

    suspend fun sendEvent(event: T) {
        _events.emit(event)
    }
}