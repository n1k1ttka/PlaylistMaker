package com.example.playlistmaker.UI.media.view_model

import android.net.Uri
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
            if (isPlaylistUpdated.value != null) {
                isPlaylistUpdated.value.let {
                    playlistInteractor.updatePlaylist(
                        Playlist(
                            id = isPlaylistUpdated.value!!.id,
                            avatarPath = avatarPath,
                            playlistName = name.value.orEmpty(),
                            description = description.value.orEmpty(),
                            tracksCount = isPlaylistUpdated.value!!.tracksCount,
                            tracksDuration = isPlaylistUpdated.value!!.tracksDuration
                        )
                    )
                }
            } else {
                playlistInteractor.addPlaylist(
                    Playlist(
                        id = 0,
                        avatarPath = avatarPath,
                        playlistName = name.value.orEmpty(),
                        description = description.value.orEmpty(),
                        tracksCount = 0,
                        tracksDuration = 0
                    )
                )
            }
        }
    }
}