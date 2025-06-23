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
                var duration: Long = 0
                val tracks = playlistTrackRepository.getTracksForPlaylist(playlistDto.id).map { track ->
                    duration += track.trackTimeMillis
                }
                Playlist(
                    id = playlistDto.id,
                    playlistName = playlistDto.playlistName,
                    avatarPath = playlistDto.avatarPath,
                    description = playlistDto.description,
                    tracksCount = tracks.size,
                    tracksDuration = duration
                )
            }
        }
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        val currentPlaylist = playlistRepository.getPlaylist(playlistId)
        var duration: Long = 0
        val tracks = playlistTrackRepository.getTracksForPlaylist(currentPlaylist.id).map { track ->
            duration += track.trackTimeMillis
        }
        return Playlist(
            id = currentPlaylist.id,
            playlistName = currentPlaylist.playlistName,
            avatarPath = currentPlaylist.avatarPath,
            description = currentPlaylist.description,
            tracksCount = tracks.size,
            tracksDuration = duration
        )
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(
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