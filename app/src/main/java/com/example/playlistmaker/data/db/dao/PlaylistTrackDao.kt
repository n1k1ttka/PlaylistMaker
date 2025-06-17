package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.data.db.entity.PlaylistTrackCrossRef
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTrackDao {

    @Transaction
    suspend fun insertTrackToPlaylist(track: TrackEntity, playlistId: Int): Long {
        insertTrack(track)
        return insertCrossRef(PlaylistTrackCrossRef(playlistId = playlistId, trackId = track.trackId))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: PlaylistTrackCrossRef): Long

    @Transaction
    suspend fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        deleteTrackFromPlaylistTrackCrossRef(playlistId, trackId)
        deleteTrackFromDataBase(trackId)
    }

    @Query("DELETE FROM PlaylistTrackCrossRef WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromPlaylistTrackCrossRef(playlistId: Int, trackId: Int)

    @Query("DELETE FROM playlist_tracks_table WHERE track_id = :trackId")
    suspend fun deleteTrackFromDataBase(trackId: Int)

    @Transaction
    suspend fun getTracksForPlaylist(playlistId: Int): List<TrackEntity> {
        val crossRefs = getCrossRefsByPlaylist(playlistId)
        val trackIds = crossRefs.map { it.trackId }
        return if (trackIds.isNotEmpty()) getTracksByIds(trackIds) else emptyList()
    }

    @Query("SELECT * FROM PlaylistTrackCrossRef WHERE playlistId = :playlistId ORDER BY created_at DESC")
    suspend fun getCrossRefsByPlaylist(playlistId: Int): List<PlaylistTrackCrossRef>

    @Query("SELECT * FROM playlist_tracks_table WHERE track_id IN (:trackIds)")
    suspend fun getTracksByIds(trackIds: List<Int>): List<TrackEntity>
}