package com.example.playlistmaker.ui.media.view_model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlayListViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {

    private val _name = MutableLiveData("")
    val name: LiveData<String> = _name

    private val _description = MutableLiveData("")
    val description: LiveData<String> = _description

    private val _isImageSelected = MutableLiveData<Uri?>()
    val isImageSelected: LiveData<Uri?> = _isImageSelected

    private val _isPlaylistUpdated = MutableLiveData<Playlist?>()
    val isPlaylistUpdated: LiveData<Playlist?> = _isPlaylistUpdated

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun setImageSelected(uri: Uri?) {
        _isImageSelected.value = uri
    }

    fun hasUnsavedChanges(): Boolean {
        return !(_name.value.isNullOrBlank() && _description.value.isNullOrBlank() && _isImageSelected.value == null)
    }

    fun isUpdate(playlist: Playlist?){
        _isPlaylistUpdated.value = playlist
    }

    fun savePlaylist(avatarPath: String) {
        viewModelScope.launch {
            val currentName = name.value.orEmpty()
            val currentDescription = description.value.orEmpty()
            val updatedPlaylist = isPlaylistUpdated.value

            val playlist = if (updatedPlaylist != null) {
                Playlist(
                    id = updatedPlaylist.id,
                    avatarPath = avatarPath,
                    playlistName = currentName,
                    description = currentDescription,
                    tracksCount = updatedPlaylist.tracksCount,
                    tracksDuration = updatedPlaylist.tracksDuration
                )
            } else {
                Playlist(
                    id = 0,
                    avatarPath = avatarPath,
                    playlistName = currentName,
                    description = currentDescription,
                    tracksCount = 0,
                    tracksDuration = 0
                )
            }

            try {
                if (updatedPlaylist != null) {
                    playlistInteractor.updatePlaylist(playlist)
                } else {
                    playlistInteractor.addPlaylist(playlist)
                }
            } catch (e: Exception) {
                Log.e("PlaylistVM", "Ошибка при сохранении плейлиста", e)
            }
        }
    }
}