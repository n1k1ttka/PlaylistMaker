package com.example.playlistmaker.Domain.db.impl

import com.example.playlistmaker.Domain.Playlist
import com.example.playlistmaker.Domain.db.PlaylistInteractor
import com.example.playlistmaker.Domain.db.PlaylistRepository
import com.example.playlistmaker.Domain.db.PlaylistTrackRepository
import com.example.playlistmaker.data.dto.PlaylistDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository,
    private val playlistTrackRepository: PlaylistTrackRepository
): PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists().map { playlistsDto ->
            playlistsDto.map { playlistDto ->
                val tracks = playlistTrackRepository.getTracksForPlaylist(playlistDto.id)
                Playlist(
                    id = playlistDto.id,
                    playlistName = playlistDto.playlistName,
                    avatarPath = playlistDto.avatarPath,
                    description = playlistDto.description,
                    tracksCount = tracks.size
                )
            }
        }
    }

    override suspend fun addPlaylist(playlist: Playlist): Long {
        return playlistRepository.addPlaylist(
            PlaylistDto(
                id = playlist.id,
                avatarPath = playlist.avatarPath,
                playlistName = playlist.playlistName,
                description = playlist.description
            )
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        return playlistRepository.deletePlaylist(
            PlaylistDto(
                id = playlist.id,
                avatarPath = playlist.avatarPath,
                playlistName = playlist.playlistName,
                description = playlist.description
            )
        )
    }
}