package edu.temple.beatbuddy.music_playlist.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.temple.beatbuddy.music_playlist.model.PlaylistSongCrossRef

@Dao
interface PlaylistSongCrossRefDao {
    @Insert
    suspend fun insertPlaylistSongCrossRef(playlistSongCrossRef: PlaylistSongCrossRef)

    @Query("DELETE FROM PlaylistSongCrossRef WHERE playlistId = :playlistId")
    suspend fun deleteCrossRefsForPlaylist(playlistId: Long)
}